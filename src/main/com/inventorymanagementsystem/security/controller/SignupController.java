package com.inventorymanagementsystem.security.controller;

import com.inventorymanagementsystem.exception.globalexception.IncorrectEmailException;
import com.inventorymanagementsystem.exception.globalexception.MissedFirstOrLastNameException;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.utils.RequestPaths;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RequestPaths.SIGHUP)
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

        ResponseBody messageBody = new ResponseBody(HttpStatus.CREATED.value(), "Signed up successfully",
                RequestPaths.SIGHUP);

        return new ResponseEntity<>(messageBody, HttpStatus.CREATED);
    }
}
