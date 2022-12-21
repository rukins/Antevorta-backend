package com.antevorta.security.service;

import com.antevorta.exception.serverexception.EntityAlreadyExistsException;
import com.antevorta.model.User;
import com.antevorta.repository.UserRepository;
import com.antevorta.security.encryptor.Encryptor;
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

    public void signup(@RequestBody User user) throws EntityAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityAlreadyExistsException(String.format("User with '%s' email already exists", user.getEmail()));
        }

        user.setPassword(encryptor.encrypt(user.getPassword()));

        userRepository.save(user);
    }
}
