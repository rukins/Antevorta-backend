package com.antevorta.exception;

import com.antevorta.exception.serverexception.ServerException;
import com.antevorta.model.ResponseBody;
import com.antevorta.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ServerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerExceptionHandler.class);

    @ExceptionHandler({ServerException.class})
    public ResponseEntity<?> handleServerException(ServerException ex, HttpServletRequest request) {
        ResponseBody body = new ResponseBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString(), ex);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
