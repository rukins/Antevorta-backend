package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.User;
import com.antevorta.service.CurrentUserService;
import com.antevorta.utils.UserJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    private final CurrentUserService currentUserService;

    @Autowired
    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<?> getUserinfo() {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        UserJsonUtils.getString(currentUserService.getAuthorizedUser())
                );
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestParam String field, HttpServletRequest request)
            throws ServerException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(
                        currentUserService.updateUser(user, field, request)
                );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify() {
        currentUserService.verify();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
