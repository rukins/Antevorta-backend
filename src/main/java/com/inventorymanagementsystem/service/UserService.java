package com.inventorymanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByLogin(String login) {
        Optional<User> user = userRepository.findByEmail(login);

        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
