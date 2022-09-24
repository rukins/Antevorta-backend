package com.inventorymanagementsystem.exception;

import com.inventorymanagementsystem.model.ResponseMessageBody;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ResponseMessageBody messageBody = new ResponseMessageBody(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");

        response.setHeader("Content-Type", "application/json");
        response.setStatus(messageBody.getStatus());
        response.getWriter().write(messageBody.toString());
    }
}
