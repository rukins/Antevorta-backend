package com.antevorta.security.service;

import com.antevorta.security.model.UserCredentials;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public AuthService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public void login(@RequestBody UserCredentials userCredentials, HttpServletRequest request) {
        Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                userCredentials.getEmail(),
                userCredentials.getPassword()
        ));

        setSessionAuth(request.getSession(), auth);
    }

    @SneakyThrows
    public void logout(HttpServletRequest request) {
        request.logout();
    }

    private void setSessionAuth(HttpSession session, Authentication auth) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }
}
