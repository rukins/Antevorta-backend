package com.antevorta.controller;

import com.antevorta.service.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userinfo")
public class UserController {
    private final CurrentUserService currentUserService;

    @Autowired
    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<?> getUserinfo() {
        return ResponseEntity.ok(currentUserService.getAuthorizedUser());
    }
}
