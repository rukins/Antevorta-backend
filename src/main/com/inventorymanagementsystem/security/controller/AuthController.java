package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.security.model.UserCredentials;
import com.inventorymanagementsystem.security.provider.CustomAuthenticationProvider;
import com.inventorymanagementsystem.utils.RequestPaths;
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
@RequestMapping(RequestPaths.AUTH)
public class AuthController {
    private final AuthenticationProvider authenticationProvider;

    public AuthController(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @PostMapping(RequestPaths.LOGIN)
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.getEmail(),
                userCredentials.getPassword()
        ));

        setSessionAuth(request.getSession(), auth);

        ResponseBody responseBody = new ResponseBody(HttpStatus.OK.value(), "Logged in successfully",
                RequestPaths.AUTH + RequestPaths.LOGIN);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(RequestPaths.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) throws ServletException {
        request.logout();

        ResponseBody responseBody = new ResponseBody(HttpStatus.OK.value(), "Logged out successfully",
                RequestPaths.AUTH + RequestPaths.LOGOUT);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    private void setSessionAuth(HttpSession session, Authentication auth) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
