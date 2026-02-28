package org.roadmapBack.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtUtilsTest {

    private final String EMAIL="test@gmail.com";
    private final String MAL_FORMATED_STRING="ooga booga";
    private  String expiredToken;
    private  String validToken;
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "thisIsAVeryLongSecretKeyThatShouldBeAtLeast256BitsLongForHS256");
        ReflectionTestUtils.setField(jwtUtils, "accessTokenExpiration", 900000L);
        ReflectionTestUtils.setField(jwtUtils, "refreshTokenExpiration", 604800000L);

        validToken=jwtUtils.generateAccessToken(EMAIL);
        ReflectionTestUtils.setField(jwtUtils, "accessTokenExpiration", -1000L);
        expiredToken = jwtUtils.generateAccessToken(EMAIL);
        ReflectionTestUtils.setField(jwtUtils, "accessTokenExpiration", 900000L);
    }

    @Test
    public void generateAccessTokenTest(){
        final var accessToken=jwtUtils.generateAccessToken(EMAIL);
        assertThat(accessToken).isNotNull();
        assertThat(accessToken).isNotEmpty();
        assertThat(jwtUtils.isTokenValid(accessToken)).isTrue();
    }

    @Test
    public void generateRefreshTokenTest(){
        final var refreshToken=jwtUtils.generateRefreshToken(EMAIL);
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
        assertThat(jwtUtils.isTokenValid(refreshToken)).isTrue();
    }

    @Test
    public void isTokenValidTest(){
        final var randomStringResult=jwtUtils.isTokenValid(MAL_FORMATED_STRING);
        assertThat(randomStringResult).isFalse();

        final var expiredTokenResult=jwtUtils.isTokenValid(expiredToken);
        assertThat(expiredTokenResult).isFalse();

        final var validTokenResult=jwtUtils.isTokenValid(validToken);
        assertThat(validTokenResult).isTrue();
    }

    @Test
    public void extractEmailTest(){
        assertThatThrownBy(() -> jwtUtils.extractEmail(MAL_FORMATED_STRING))
                .isInstanceOf(MalformedJwtException.class);

        assertThatThrownBy(() -> jwtUtils.extractEmail(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);

        final var validTokenResult=jwtUtils.extractEmail(validToken);
        assertThat(validTokenResult).isEqualTo(EMAIL);
    }
}