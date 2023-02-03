package com.antevorta.service;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.exception.serverexception.WrongVerificationCodeException;
import com.antevorta.repository.redis.VerificationCodeRepository;
import com.antevorta.service.email.EmailSender;
import com.mailgun.model.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EmailVerificationService {
    @Value("${redis.key-prefix.verification-code}")
    private String keyPrefix;

    @Value("${redis.time-to-live.verification-code}")
    private Long timeToLive;

    private final EmailSender emailSender;

    private final CurrentUserService currentUserService;

    private final VerificationCodeRepository verificationCodeRepository;

    @Autowired
    public EmailVerificationService(EmailSender emailSender,
                                    CurrentUserService currentUserService,
                                    VerificationCodeRepository verificationCodeRepository) {
        this.emailSender = emailSender;
        this.currentUserService = currentUserService;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    public void sendVerificationCode() {
        String email = currentUserService.getAuthorizedUser().getEmail();

        Integer verificationCode = generateVerificationCode();

        verificationCodeRepository.save(keyPrefix, verificationCode, timeToLive, email);

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

    public void verifyUserIfVerificationCodeIsCorrect(Integer receivedVerificationCode) throws ServerException {
        if (isCorrect(receivedVerificationCode)) {
            currentUserService.verifyUser();

            return;
        }

        throw new WrongVerificationCodeException("Wrong verification code");
    }

    private boolean isCorrect(Integer receivedVerificationCode) {
        Integer realVerificationCode = verificationCodeRepository
                .getByKeyPrefix(keyPrefix, currentUserService.getAuthorizedUser().getEmail());

        if (receivedVerificationCode.equals(realVerificationCode)) {
            return true;
        }

        return false;
    }

    private Integer generateVerificationCode() {
        Random random = new Random();

        return Integer.parseInt(
                random.ints(6, 0, 10).mapToObj(String::valueOf).collect(Collectors.joining())
        );
    }
}
