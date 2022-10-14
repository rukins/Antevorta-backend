package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByLogin(String login) {
        Optional<User> user = userRepository.findByEmail(login);

        return user.orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
