package com.inventorymanagementsystem.security.service;

import com.inventorymanagementsystem.exception.serverexception.EntityAlreadyExistsException;
import com.inventorymanagementsystem.exception.serverexception.MissedFirstOrLastNameException;
import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import com.inventorymanagementsystem.security.encryptor.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SignupService {
    private final UserRepository userRepository;

    private final Encryptor encryptor;

    @Autowired
    public SignupService(UserRepository userRepository, Encryptor encryptor) {
        this.userRepository = userRepository;
        this.encryptor = encryptor;
    }

    public void signup(@RequestBody User user) throws EntityAlreadyExistsException, MissedFirstOrLastNameException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityAlreadyExistsException(String.format("User with '%s' email already exists", user.getEmail()));
        }

        if (user.getFirstname() == null || user.getLastname() == null
                || user.getFirstname().isEmpty() || user.getLastname().isEmpty()) {
            throw new MissedFirstOrLastNameException("Please enter your first and last names. One of them or both are empty");
        }

        user.setPassword(encryptor.encrypt(user.getPassword()));

        userRepository.save(user);
    }
}
