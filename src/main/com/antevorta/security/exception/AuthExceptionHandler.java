package com.antevorta.security.exception;

import com.antevorta.model.ResponseBody;
import com.antevorta.security.exception.authexception.UserNotAuthenticatedException;
import com.antevorta.security.exception.authexception.WrongPasswordException;
import com.antevorta.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AuthExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler({UserNotAuthenticatedException.class})
    public ResponseEntity<?> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({WrongPasswordException.class})
    public ResponseEntity<?> handleWrongPasswordException(WrongPasswordException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.FORBIDDEN.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
