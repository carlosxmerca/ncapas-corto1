package com.carlosxmerca.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private String names;
    private String lastNames;
    private String code;
    private String pass;
    private String dateOfHire;
    private boolean isAdmin;
    private boolean isActive;

}