package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.security.model.UserCredentials;
import com.inventorymanagementsystem.security.service.AuthService;
import com.inventorymanagementsystem.utils.RequestPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(RequestPaths.AUTH)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(RequestPaths.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        authService.login(userCredentials, request);

        ResponseBody responseBody = new ResponseBody(HttpStatus.OK.value(), "Logged in successfully",
                RequestPaths.AUTH + RequestPaths.LOGIN);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(RequestPaths.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) throws ServletException {
        authService.logout(request);

        ResponseBody responseBody = new ResponseBody(HttpStatus.OK.value(), "Logged out successfully",
                RequestPaths.AUTH + RequestPaths.LOGOUT);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
