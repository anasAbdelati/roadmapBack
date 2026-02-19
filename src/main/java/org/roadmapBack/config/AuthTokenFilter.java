package org.roadmapBack.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.roadmapBack.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    AuthTokenFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");

        final String token;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        //remove  "Bearer "
        token = authHeader.substring(7);
        email = jwtUtils.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var user = userService.findUserByEmail(email);

            if (jwtUtils.isTokenValid(token)) {
                //TODO add roles later to User ok
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
