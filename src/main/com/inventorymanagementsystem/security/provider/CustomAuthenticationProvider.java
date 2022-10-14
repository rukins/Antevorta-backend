package com.inventorymanagementsystem.security.provider;

import com.inventorymanagementsystem.exception.authexception.WrongLoginOrPasswordException;
import com.inventorymanagementsystem.security.service.CustomUserDetailsService;
import com.inventorymanagementsystem.utils.RequestPaths;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

 @Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongLoginOrPasswordException("Wrong password", RequestPaths.AUTH + RequestPaths.LOGIN);
        }

        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }

     @Override
     public boolean supports(Class<?> authentication) {
         return authentication.equals(UsernamePasswordAuthenticationToken.class);
     }
 }
