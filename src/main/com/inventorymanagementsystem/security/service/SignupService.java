package com.inventorymanagementsystem.security.service;

import com.inventorymanagementsystem.exception.globalexception.IncorrectEmailException;
import com.inventorymanagementsystem.exception.globalexception.MissedFirstOrLastNameException;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.utils.RequestPaths;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SignupService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    public void signup(@RequestBody User user) {
        if (user.getEmail() == null || !user.hasValidEmail()) {
            throw new IncorrectEmailException("Incorrect email", RequestPaths.SIGHUP);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new MissedFirstOrLastNameException(String.format("User with %s email already exists", user.getEmail()),
                    RequestPaths.SIGHUP);
        }

        if (user.getFirstname() == null || user.getLastname() == null
                || user.getFirstname().isEmpty() || user.getLastname().isEmpty()) {
            throw new MissedFirstOrLastNameException("Please enter your first and last names. One of them or both are empty",
                    RequestPaths.SIGHUP);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }
}
