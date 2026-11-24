//package com.bankingApp.banking_app.repository;
//
//import com.bankingApp.banking_app.repository.entity.BankUser;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
//public interface BankUserRepository extends JpaRepository<BankUser, Integer> {
//
//    @Query("""
//        Join multimple tables and return user details
//    """)
//    Optional<BankUser> findByIdWithDetails(@Param("id") Integer id);
//
//
//}
