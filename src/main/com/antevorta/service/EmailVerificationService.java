package com.antevorta.service;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.exception.serverexception.WrongVerificationCodeException;
import com.antevorta.repository.redis.StringRepository;
import com.antevorta.service.email.EmailSender;
import com.antevorta.utils.RedisKeyUtils;
import com.mailgun.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EmailVerificationService {
    @Value("${redis.time-to-live.verification-code}")
    private Long timeToLive;

    private final EmailSender emailSender;

    private final CurrentUserService currentUserService;

    private final StringRepository stringRepository;

    @Autowired
    public EmailVerificationService(EmailSender emailSender,
                                    CurrentUserService currentUserService,
                                    StringRepository stringRepository) {
        this.emailSender = emailSender;
        this.currentUserService = currentUserService;
        this.stringRepository = stringRepository;
    }

    public void sendVerificationCode() {
        String email = currentUserService.getAuthorizedUser().getEmail();

        String verificationCode = generateVerificationCode();

        stringRepository.save(RedisKeyUtils.getForVerificationCode(), verificationCode, timeToLive);

        emailSender.send(
                Message.builder()
                        .from(
                                String.format("Antevorta <antevorta@%s>", emailSender.getDomain())
                        )
                        .to(email)
                        .subject("Email verification")
                        .text("Your verification code: " + verificationCode)
                        .build()
        );
    }

    public void verifyUserIfVerificationCodeIsCorrect(String receivedVerificationCode) throws ServerException {
        if (isCorrect(receivedVerificationCode)) {
            currentUserService.verifyUser();
            return;
        }

        throw new WrongVerificationCodeException("Wrong verification code");
    }

    private boolean isCorrect(String receivedVerificationCode) {
        String realVerificationCode = stringRepository.get(
                RedisKeyUtils.getForVerificationCode()
        );

        if (receivedVerificationCode.equals(realVerificationCode)) {
            return true;
        }

        return false;
    }

    private String generateVerificationCode() {
        Random random = new Random();

        return random
                .ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
