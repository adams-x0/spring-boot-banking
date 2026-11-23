package com.bankingApp.banking_app.controller;


import com.bankingApp.banking_app.controller.dto.AccountResponse;
import com.bankingApp.banking_app.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class AccouontController {

    private final AccountService studentService;

    public AccouontController(AccountService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createBankUser(@Valid @RequestBody CreateBankUserRequest request){
//        AccountResponse response = studentService.registerStudent(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


//    add more methods below

}
