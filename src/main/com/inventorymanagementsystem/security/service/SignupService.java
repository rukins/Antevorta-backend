package com.inventorymanagementsystem.security.service;

import com.inventorymanagementsystem.exception.globalexception.IncorrectEmailException;
import com.inventorymanagementsystem.exception.globalexception.MissedFirstOrLastNameException;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import com.inventorymanagementsystem.utils.RequestUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SignupService {
    private final UserRepository userRepository;

    private final Encryptor encryptor;

    public SignupService(UserRepository userRepository, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    @SneakyThrows
    public void signup(@RequestBody User user) {
        if (user.getEmail() == null || !user.hasValidEmail()) {
            throw new IncorrectEmailException("Incorrect email", RequestUtils.SIGHUP_PATH);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new MissedFirstOrLastNameException(String.format("User with '%s' email already exists", user.getEmail()),
                    RequestUtils.SIGHUP_PATH);
        }

        if (user.getFirstname() == null || user.getLastname() == null
                || user.getFirstname().isEmpty() || user.getLastname().isEmpty()) {
            throw new MissedFirstOrLastNameException("Please enter your first and last names. One of them or both are empty",
                    RequestUtils.SIGHUP_PATH);
        }

        user.setPassword(encryptor.encrypt(user.getPassword()));

        userRepository.save(user);
    }
}
