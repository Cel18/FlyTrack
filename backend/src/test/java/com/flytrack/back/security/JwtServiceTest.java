package com.flytrack.back.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = "my-very-long-and-secure-secret-key-that-must-be-at-least-256-bits";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 86400000L);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken("test@test.com", "ADMIN");
        assertNotNull(token);
        assertEquals("test@test.com", jwtService.extractCorreo(token));
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {
        String token = jwtService.generateRefreshToken("test@test.com");
        assertNotNull(token);
        assertEquals("test@test.com", jwtService.extractCorreo(token));
    }

    @Test
    void isTokenValid_WithInvalidToken_ShouldReturnFalse() {
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }
}
