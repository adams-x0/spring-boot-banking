--CREATE DATABASE banking_p1;

begin;

--Bank application users are seperate from PostgreSQL Login users.

create table bank_users (
                            user_id bigint generated always as identity primary key,
                            username varchar(50) not null,
                            password_hash varchar(255) not null,
                            first_name varchar(100) not null,
                            last_name varchar(100) not null,
                            email varchar(254) not null,
                            role varchar(10) not null default 'CUSTOMER',
                            created_at timestamptz not null default current_timestamp,

                            constraint uq_bank_users_username unique (username),

                            constraint ck_bank_users_username
                                check (length(trim(username)) > 0),

                            constraint ck_bank_users_password_hash
                                check (length(trim(password_hash)) > 0),

                            constraint ck_bank_users_first_name
                                check (length(trim(first_name)) > 0),

                            constraint ck_bank_users_last_name
                                check (length(trim(last_name)) > 0),

                            constraint ck_bank_users_email
                                check (length(trim(email)) > 0),

                            constraint ck_bank_users_role
                                check (role in ('CUSTOMER', 'EMPLOYEE', 'ADMIN'))


);

create table accounts (
                          account_id bigint generated always as identity primary key,
                          account_type varchar(10) not null default 'INDIVIDUAL',
                          balance numeric(15, 2) not null default 0.00,
                          status varchar(10) not null default 'PENDING',

                          applied_by bigint not null,
                          applied_at timestamptz not null default current_timestamp,

                          reviewed_by bigint,
                          reviewed_at timestamptz,

                          cancelled_by bigint,
                          cancelled_at timestamptz,

                          constraint fk_accounts_applicant
                              foreign key (applied_by) references bank_users(user_id),

                          constraint fk_accounts_reviewer
                              foreign key (reviewed_by) references bank_users(user_id),


                          constraint fk_accounts_canceller
                              foreign key (cancelled_by) references bank_users(user_id),

                          constraint ck_accounts_type
                              check (account_type in ('INDIVIDUAL', 'JOINT')),

                          constraint ck_accounts_status
                              check (
                                  status in ('PENDING', 'APPROVED', 'DENIED', 'CANCELLED')
                                  ),

                          constraint ck_accounts_balance
                              check (
                                  balance >= 0
                                      and balance <= 9999999999999.99
                                      and balance <> 'NaN'::NUMERIC
),

--only approved accounts can hold money.
	constraint ck_accounts_inactive_balance
		check (status = 'APPROVED' or balance = 0),

--Reviewer identity and timestamp must be supplied together.
	constraint ck_accounts_review_pair
		check (
			(reviewed_by is null and reviewed_at is null)
			or
			(reviewed_by is not null and reviewed_at is not null)

		),

	constraint ck_accounts_review_status
		check (
			(status = 'PENDING'
				and reviewed_by is null
				and reviewed_at is null)
			or
			(status in ('APPROVED', 'DENIED')
				and reviewed_by is not null
				and reviewed_at is not null)

			or
			status  = 'CANCELLED'
		),

--A cancelled pending application may have no review.
--A previously reviewed account retains its review information.

	constraint ck_accounts_cancellation
		check (
			(status = 'CANCELLED'
				and cancelled_by is not null
				and cancelled_at is not null)
			or
			(status <> 'CANCELLED'
				and cancelled_by is null
				and cancelled_at is null)
		),

	constraint ck_accounts_review_time
		check (reviewed_at is null or reviewed_at >= applied_at),

	constraint ck_accounts_cancellation_time
		check (
			cancelled_at is null
			or (
				cancelled_at >= applied_at
				and (
					reviewed_at is null
					or cancelled_at >= reviewed_at
				)
			)
		)
);

--An account can have several owners, and a user can own several accounts.
create table account_owners (
                                account_id bigint not null,
                                user_id bigint not null,
                                ownership_status varchar(10) not null default 'INVITED',
                                invited_at timestamptz not null default current_timestamp,
                                responded_at timestamptz,

                                constraint pk_account_owners
                                    primary key (account_id, user_id),

                                constraint fk_account_owners_account
                                    foreign key (account_id) references accounts(account_id),

                                constraint fk_account_owners_user
                                    foreign key (user_id) references bank_users(user_id),

                                constraint ck_account_owners_status
                                    check (
                                        ownership_status in ('INVITED', 'ACCEPTED', 'DECLINED')
                                        ),

                                constraint ck_account_owners_response
                                    check (
                                        (ownership_status = 'INVITED' and responded_at is null)
                                            or
                                        (ownership_status in ('ACCEPTED', 'DECLINED')
                                            and responded_at is not null)
                                        ),

                                constraint ck_account_owners_response_time
                                    check (
                                        responded_at is null
                                            or responded_at >= invited_at
                                        )
);

create table bank_transactions (
                                   transaction_id bigint generated always as identity primary key,
                                   transaction_type varchar(10) not null,
                                   amount numeric(15, 2) not null,
                                   source_account_id bigint,
                                   destination_account_id bigint,
                                   performed_by bigint not null,
                                   created_at timestamptz not null default current_timestamp,

                                   constraint fk_transactions_source
                                       foreign key (source_account_id) references accounts(account_id),

                                   constraint fk_transactions_destination
                                       foreign key (destination_account_id) references accounts(account_id),

                                   constraint fk_transactions_performer
                                       foreign key (performed_by) references bank_users(user_id),

                                   constraint ck_transactions_amount
                                       check (
                                           amount > 0
                                               and amount <= 9999999999999.99
                                               and amount <> 'NaN'::NUMERIC
),

--each operation requires a different combination of account references.
	constraint ck_transaction_accounts
		check (
			(
				transaction_type = 'DEPOSIT'
				and source_account_id is null
				and destination_account_id is not null
			)
			or
			(
				transaction_type = 'WITHDRAWAL'
				and source_account_id is not null
				and destination_account_id is null
			)
			or
			(
				transaction_type = 'TRANSFER'
				and source_account_id is not null
				and destination_account_id is not null
				and source_account_id <> destination_account_id
			)
		)
);

-- The ownership primary key already indexes account_id first.
-- This additional index supports looking up a user's account.

create index idx_account_owners_user
    on account_owners(user_id);

create index idx_accounts_status_applied
    on accounts(status, applied_at);

create index idx_transactions_source_created
    on bank_transactions(source_account_id, created_at);

create index idx_transactions_destination_created
    on bank_transactions(destination_account_id, created_at);

commit;