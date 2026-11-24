package com.bankingApp.banking_app.controller.dto;


import java.util.ArrayList;
import java.util.List;

public class AccountResponse {

    private Integer id;
    private String name;
    private String departmentName;
    private String bio;
    private List<String> enrolledCourseTitles = new ArrayList<>();

    public AccountResponse() {
    }

    public AccountResponse(Integer id, String name, String departmentName, String bio, List<String> enrolledCourseTitles) {
        this.id = id;
        this.name = name;
        this.departmentName = departmentName;
        this.bio = bio;
        this.enrolledCourseTitles = enrolledCourseTitles != null ? enrolledCourseTitles : new ArrayList<>();
    }


}
