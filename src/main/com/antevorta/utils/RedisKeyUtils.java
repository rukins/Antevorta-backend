package com.antevorta.utils;

import com.antevorta.context.SpringContext;
import com.antevorta.service.CurrentUserService;

public class RedisKeyUtils {

    private static final String verificationCodeKeyPrefix = SpringContext.getProperty("redis.key-prefix.verification-code");

    private static final String ebayAccessTokenKeyPrefix = SpringContext.getProperty("redis.key-prefix.onlinestores.ebay");

    private static final CurrentUserService currentUserService = SpringContext.getBean(CurrentUserService.class);

    public static String getForVerificationCode() {
        return verificationCodeKeyPrefix + currentUserService.getAuthorizedUser().getEmail();
    }

    public static String getForOnlineStoreAccessKey(String arbitraryStoreName) {
        return ebayAccessTokenKeyPrefix + currentUserService.getAuthorizedUser().getEmail() + ":" + arbitraryStoreName;
    }
}
