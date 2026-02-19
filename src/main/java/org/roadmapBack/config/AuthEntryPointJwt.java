package org.roadmapBack.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //TODO change status or test it
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Invalid or missing JWT token\"}");
    }
}