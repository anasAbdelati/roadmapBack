package org.roadmapBack.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roadmapBack.config.JwtUtils;
import org.roadmapBack.data.User;
import org.roadmapBack.dto.LoginRequestDto;
import org.roadmapBack.dto.RefreshRequestDto;
import org.roadmapBack.exceptions.InvalidRefreshTokenException;
import org.roadmapBack.exceptions.WrongcredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    public static final String EMAIL = "test@gmail.com";
    public static final String PASSWORD = "password123";
    public static final String HASHED_PASSWORD = "hashedPassword";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private AuthService authService;

    @Test
    public void loginTest(){
        final var request = LoginRequestDto.builder().email(EMAIL).password(PASSWORD).build();
        final var user = User.builder().email(EMAIL).password(HASHED_PASSWORD).build();
        when(userService.findUserByEmail(EMAIL)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(WrongcredentialsException.class);

        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtils.generateAccessToken(EMAIL)).thenReturn(ACCESS_TOKEN);
        when(jwtUtils.generateRefreshToken(EMAIL)).thenReturn(REFRESH_TOKEN);

        final var result = authService.login(request);
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }

    @Test
    public void refreshTest(){
        final var request = RefreshRequestDto.builder().refreshToken(REFRESH_TOKEN).build();

        when(jwtUtils.isTokenValid(REFRESH_TOKEN)).thenReturn(false);
        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(InvalidRefreshTokenException.class);

        when(jwtUtils.isTokenValid(REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtils.extractEmail(REFRESH_TOKEN)).thenReturn(EMAIL);
        when(jwtUtils.generateAccessToken(EMAIL)).thenReturn(ACCESS_TOKEN);
        final var result = authService.refresh(request);
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }
}
