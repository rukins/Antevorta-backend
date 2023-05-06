package com.antevorta.security.service;

import com.antevorta.model.User;
import com.antevorta.repository.UserRepository;
import com.antevorta.security.exception.authexception.UserNotAuthenticatedException;
import com.antevorta.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new UserNotAuthenticatedException(String.format("User with '%s' email not authenticated", email)));
    }
}
