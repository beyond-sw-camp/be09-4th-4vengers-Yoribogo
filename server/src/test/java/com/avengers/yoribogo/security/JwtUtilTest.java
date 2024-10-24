package com.avengers.yoribogo.security;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.avengers.yoribogo.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; // DisplayName 추가
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtUtilTest {

    @Mock
    private UserService userService;

    private JwtUtil jwtUtil;

    //설명. 임의의 base 64키 생성
    private String secretKey = "KksWkOy539IOOrQCShB0fI0KuhODzPnXRYmpY7rjEpF4TjzQ/Mo9r1+LMnYja+QFISDBF5ps6A312VqK4tfnZQ==";
    private Key signingKey;
    private long accessExpirationTime = 1000 * 60 * 15; // 15 minutes
    private long refreshExpirationTime = 1000 * 60 * 60 * 24; // 24 hours

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        jwtUtil = new JwtUtil(secretKey, accessExpirationTime, refreshExpirationTime, userService);
    }

    @Test
    @DisplayName("토큰 생성 테스트") // Test for generating a token
    public void testGenerateToken() {
        // given
        UserEntity user = new UserEntity();
        user.setUserAuthId("user123");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.ADMIN);

        // when
        String token = jwtUtil.generateToken(user, Collections.singletonList("ROLE_ADMIN"));

        // then
        assertNotNull(token);
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        assertEquals("user123", claims.getSubject());
        assertEquals("user@example.com", claims.get("email"));
        assertEquals("ROLE_ADMIN", claims.get("auth").toString().replace("[", "").replace("]", ""));
    }

    @Test
    @DisplayName("유효한 토큰 검증 테스트") // Test for validating a valid token
    public void testValidateToken_ValidToken() {
        // given
        UserEntity user = new UserEntity();
        user.setUserAuthId("user123");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.ADMIN);
        String token = jwtUtil.generateToken(user, Collections.singletonList("ROLE_ADMIN"));

        // when
        boolean isValid = jwtUtil.validateToken(token);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("잘못된 토큰 검증 테스트") // Test for validating an invalid token
    public void testValidateToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when / then
        CommonException exception = assertThrows(CommonException.class, () -> jwtUtil.validateToken(invalidToken));
        assertEquals(ErrorCode.INVALID_TOKEN_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰으로부터 인증 정보 가져오기 테스트") // Test for getting authentication from token
    public void testGetAuthentication() {
        // given
        UserEntity user = new UserEntity();
        user.setUserAuthId("user123");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.ADMIN);
        String token = jwtUtil.generateToken(user, Collections.singletonList("ROLE_ADMIN"));

        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userService.loadUserByUsername("user123")).thenReturn(userDetailsMock);

        // when
        Authentication authentication = jwtUtil.getAuthentication(token);

        // then
        assertNotNull(authentication);
        assertEquals(userDetailsMock, authentication.getPrincipal());
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 테스트") // Test for extracting user ID from token
    public void testGetUserIdFromToken() {
        // given
        UserEntity user = new UserEntity();
        user.setUserAuthId("user123");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.ADMIN);
        String token = jwtUtil.generateToken(user, Collections.singletonList("ROLE_ADMIN"));

        // when
        String userAuthId = jwtUtil.getUserId(token);

        // then
        assertEquals("user123", userAuthId);
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트") // Test for generating refresh token
    public void testGenerateRefreshToken() {
        // given
        UserEntity user = new UserEntity();
        user.setUserAuthId("user123");
        user.setEmail("user@example.com");
        user.setUserRole(UserRole.ADMIN);

        // when
        String refreshToken = jwtUtil.generateRefreshToken(user, Collections.singletonList("ROLE_ADMIN"));

        // then
        assertNotNull(refreshToken);
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(refreshToken).getBody();
        assertEquals("user123", claims.getSubject());
    }

    @Test
    @DisplayName("토큰 만료 시간 테스트") // Test for checking token expiration times
    public void testTokenExpirationTimes() {
        // when
        long accessTokenExpTime = jwtUtil.getAccessTokenExpiration();
        long refreshTokenExpTime = jwtUtil.getRefreshTokenExpiration();

        // then
        assertTrue(accessTokenExpTime > System.currentTimeMillis());
        assertTrue(refreshTokenExpTime > System.currentTimeMillis());
    }
}
