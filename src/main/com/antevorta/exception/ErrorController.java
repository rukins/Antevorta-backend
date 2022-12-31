package com.antevorta.exception;

import com.antevorta.model.ResponseBody;
import com.antevorta.utils.RequestUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorController extends BasicErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    public ErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, serverProperties.getError());
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("status", status.value());
        body.put("message", status.toString());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(
                new ResponseBody(status.value(), status.toString()).toString(),
                request.getAttribute(RequestDispatcher.ERROR_EXCEPTION)
        );

        return new ResponseEntity<>(body, status);
    }
}
