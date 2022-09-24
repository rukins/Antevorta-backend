package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseMessageBody;
import com.inventorymanagementsystem.security.model.UserCredentials;
import com.inventorymanagementsystem.security.provider.CustomAuthenticationProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationProvider authenticationProvider;

    public AuthController(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.getEmail(),
                userCredentials.getPassword()
        ));

        setSessionAuth(request.getSession(), auth);

        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.OK.value(), "Logged in successfully");

        return new ResponseEntity<>(messageBody, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws ServletException {
        request.logout();

        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.OK.value(), "Logged out successfully");

        return new ResponseEntity<>(messageBody, HttpStatus.OK);
    }

    private void setSessionAuth(HttpSession session, Authentication auth) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
