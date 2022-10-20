package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.security.service.SignupService;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/signup")
public class SignupController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody User user, HttpServletRequest request) {
        signupService.signup(user);

        ResponseBody body = new ResponseBody(HttpStatus.CREATED.value(), "Signed up successfully");

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
