package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.exception.IncorrectEmailException;
import com.inventorymanagementsystem.exception.MissedFirstOrLastNameException;
import com.inventorymanagementsystem.model.ResponseMessageBody;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<?> signup(@RequestBody User user) {
        if (user.getEmail() == null || !user.hasValidEmail()) {
            throw new IncorrectEmailException("Incorrect email");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new MissedFirstOrLastNameException(String.format("User with %s email already exists", user.getEmail()));
        }

        if (user.getFirstname() == null || user.getLastname() == null
                || user.getFirstname().isEmpty() || user.getLastname().isBlank()) {
            throw new MissedFirstOrLastNameException("Please enter your first and last names. One of them or both are empty");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.CREATED.value(), "Signed up successfully");

        return new ResponseEntity<>(messageBody, HttpStatus.CREATED);
    }
}
