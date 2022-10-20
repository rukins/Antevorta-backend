package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.exception.authexception.AuthException;
import com.inventorymanagementsystem.exception.globalexception.GlobalException;
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
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({AuthException.class})
    public ResponseEntity<?> handleWrongLoginOrPasswordException(AuthException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({GlobalException.class})
    public ResponseEntity<?> handleGlobalException(GlobalException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
