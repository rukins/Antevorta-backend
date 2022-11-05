package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.security.model.UserCredentials;
import com.inventorymanagementsystem.security.service.AuthService;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        authService.login(userCredentials, request);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(), "Logged in successfully");

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);

        ResponseBody body = new ResponseBody(HttpStatus.OK.value(), "Logged out successfully");

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
