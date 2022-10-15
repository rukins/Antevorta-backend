package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.security.service.SignupService;
import com.inventorymanagementsystem.utils.RequestPaths;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RequestPaths.SIGHUP)
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody User user) {
        signupService.signup(user);

        ResponseBody messageBody = new ResponseBody(HttpStatus.CREATED.value(), "Signed up successfully",
                RequestPaths.SIGHUP);

        return new ResponseEntity<>(messageBody, HttpStatus.CREATED);
    }
}
