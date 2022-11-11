package com.antevorta.service;

import com.antevorta.exception.serverexception.EntityNotFoundException;
import com.antevorta.model.User;
import com.antevorta.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    @Autowired
    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    public User getAuthorizedUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
