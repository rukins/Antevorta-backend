package com.inventorymanagementsystem.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {
    private String email;
    private String password;
}
