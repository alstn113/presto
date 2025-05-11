package com.presto.server.infra.security.jwt;

import com.presto.server.application.auth.TokenProvider;
import com.presto.server.infra.security.jwt.exception.BlankTokenException;
import com.presto.server.infra.security.jwt.exception.InvalidTokenException;
import com.presto.server.infra.security.jwt.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final Long expirationTime;

    public JwtTokenProvider(JwtTokenProperties properties) {
        this.key = Keys.hmacShaKeyFor(properties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.expirationTime = properties.expirationTime();
    }

    public String createToken(String memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String getMemberId(String token) {
        Claims claims = toClaims(token);

        return claims.getSubject();
    }

    private Claims toClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new BlankTokenException();
        }

        try {
            Jws<Claims> claimsJws = getClaimsJws(token);

            return claimsJws.getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e);
        } catch (JwtException e) {
            throw new InvalidTokenException(e);
        }
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}