package com.jigit.backend.global.util;

import com.jigit.backend.global.exception.ApplicationException;
import com.jigit.backend.user.exception.UserException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility class for JWT token operations
 * Handles token generation, validation, and parsing
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    /**
     * Generate JWT token for a user
     * @param userId the user ID to include in the token
     * @return generated JWT token string
     */
    public String generateToken(Long userId) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime);

            return Jwts.builder()
                    .subject(String.valueOf(userId))
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(secretKey)
                    .compact();
        } catch (Exception e) {
            throw new ApplicationException(UserException.TOKEN_GENERATION_FAILED);
        }
    }

    /**
     * Extract user ID from JWT token
     * @param token the JWT token string
     * @return the user ID extracted from the token
     * @throws ApplicationException if token is invalid or expired
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(UserException.EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApplicationException(UserException.INVALID_TOKEN);
        }
    }

    /**
     * Validate JWT token
     * @param token the JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extract token from Authorization header
     * @param authorizationHeader the Authorization header value
     * @return the extracted token, or null if header is invalid
     */
    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
