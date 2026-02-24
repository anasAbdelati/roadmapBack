package org.roadmapBack.config;

import org.roadmapBack.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/user/register",
            "/auth/login",
            "/auth/refresh"
    };
    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final UserService userService;

    SecurityConfiguration(AuthTokenFilter authTokenFilter, AuthEntryPointJwt authEntryPointJwt, UserService userService) {
        this.authTokenFilter = authTokenFilter;
        this.authEntryPointJwt = authEntryPointJwt;
        this.userService = userService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPointJwt))
            .sessionManagement(session -> session
                .sessionCreationPolicy(STATELESS))
            .userDetailsService(userService)
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}