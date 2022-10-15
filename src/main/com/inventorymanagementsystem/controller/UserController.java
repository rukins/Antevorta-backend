package com.inventorymanagementsystem.controller;

import com.inventorymanagementsystem.model.User;
import com.inventorymanagementsystem.service.UserService;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineStoreController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }   

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<User> body = userService.getAll();

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());

        return ResponseEntity.ok(body);
    }
}
