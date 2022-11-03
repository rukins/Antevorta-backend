package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.exception.authexception.AuthException;
import com.inventorymanagementsystem.exception.serverexception.ServerException;
import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ServerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerExceptionHandler.class);

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<?> handleAuthException(AuthException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ServerException.class})
    public ResponseEntity<?> handleServerException(ServerException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
