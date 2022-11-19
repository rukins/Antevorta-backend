package com.antevorta.security.controller;

import com.antevorta.model.ResponseBody;
import com.antevorta.security.model.UserCredentials;
import com.antevorta.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        authService.login(userCredentials, request);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(), "Logged in successfully")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(), "Logged out successfully")
        );
    }
}
