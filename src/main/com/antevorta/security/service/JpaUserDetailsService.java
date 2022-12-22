package com.antevorta.security.service;

import com.antevorta.exception.serverexception.EntityNotFoundException;
import com.antevorta.model.User;
import com.antevorta.repository.UserRepository;
import com.antevorta.security.model.SecurityUser;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with '%s' email not found", email)));
    }
}
