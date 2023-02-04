package com.antevorta.controller;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.ResponseBody;
import com.antevorta.model.User;
import com.antevorta.service.CurrentUserService;
import com.antevorta.service.EmailVerificationService;
import com.antevorta.utils.UserJsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final CurrentUserService currentUserService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public UserController(CurrentUserService currentUserService, EmailVerificationService emailVerificationService) {
        this.currentUserService = currentUserService;
        this.emailVerificationService = emailVerificationService;
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

    @PostMapping("/verification/verify")
    public ResponseEntity<?> verify(@RequestParam("verificationCode") String verificationCode) throws ServerException {
        if (currentUserService.isUserVerified()) {
            return new ResponseEntity<>(
                    new ResponseBody(HttpStatus.BAD_REQUEST.value(),"User email has already been verified"),
                    HttpStatus.BAD_REQUEST
            );
        }

        emailVerificationService.verifyUserIfVerificationCodeIsCorrect(verificationCode);

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(),
                        "User email has been successfully verified")
        );
    }

    @PostMapping("/verification/mail")
    public ResponseEntity<?> sendVerificationCode() {
        if (currentUserService.isUserVerified()) {
            return new ResponseEntity<>(
                    new ResponseBody(HttpStatus.BAD_REQUEST.value(),"User email has already been verified"),
                    HttpStatus.BAD_REQUEST
            );
        }

        emailVerificationService.sendVerificationCode();

        return ResponseEntity.ok(
                new ResponseBody(HttpStatus.OK.value(),
                        "Verification code has been successfully sent")
        );
    }
}
