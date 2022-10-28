package com.inventorymanagementsystem.security.provider;

import com.inventorymanagementsystem.exception.authexception.WrongPasswordException;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import com.inventorymanagementsystem.security.service.CustomUserDetailsService;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

 @Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Encryptor encryptor;
    private final CustomUserDetailsService userDetailsService;

    public CustomAuthenticationProvider(Encryptor encryptor, CustomUserDetailsService userDetailsService) {
        this.encryptor = encryptor;
        this.userDetailsService = userDetailsService;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!encryptor.matches(password, user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }

     @Override
     public boolean supports(Class<?> authentication) {
         return authentication.equals(UsernamePasswordAuthenticationToken.class);
     }
 }
