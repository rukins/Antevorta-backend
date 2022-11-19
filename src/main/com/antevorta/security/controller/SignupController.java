package com.antevorta.security.controller;

import com.antevorta.exception.serverexception.EntityAlreadyExistsException;
import com.antevorta.exception.serverexception.MissedFirstOrLastNameException;
import com.antevorta.model.ResponseBody;
import com.antevorta.model.User;
import com.antevorta.security.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {
    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody User user)
            throws EntityAlreadyExistsException, MissedFirstOrLastNameException {
        signupService.signup(user);

        return new ResponseEntity<>(
                new ResponseBody(HttpStatus.CREATED.value(), "Signed up successfully"),
                HttpStatus.CREATED
        );
    }
}
