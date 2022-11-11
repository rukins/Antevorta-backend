package com.antevorta.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentials {
    private String email;
    private String password;
}
