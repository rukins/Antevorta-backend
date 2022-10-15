package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.model.ResponseBody;
import com.inventorymanagementsystem.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ResponseBody body = new ResponseBody(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(),
                request.getRequestURI());

        response.setHeader("Content-Type", "application/json");
        response.setStatus(body.getStatus());
        response.getWriter().write(body.toString());

        logger.debug(request.getMethod());
        logger.debug(RequestUtils.getHeadersString(request));
        logger.debug(body.toString());
    }
}
