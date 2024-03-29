package com.antevorta.security.provider;

import com.antevorta.security.encryptor.Encryptor;
import com.antevorta.security.exception.authexception.WrongPasswordException;
import com.antevorta.security.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Encryptor encryptor;
    private final JpaUserDetailsService userDetailsService;

    @Autowired
    public CustomAuthenticationProvider(Encryptor encryptor, JpaUserDetailsService userDetailsService) {
        this.encryptor = encryptor;
        this.userDetailsService = userDetailsService;
    }

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
