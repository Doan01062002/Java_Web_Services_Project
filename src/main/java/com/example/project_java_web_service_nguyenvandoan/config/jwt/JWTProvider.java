package com.example.project_java_web_service_nguyenvandoan.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    private final Key jwtSecret;
    @Value("${jwt_expire}")
    private int jwtExpire;

    @Value("${jwt_refresh}")
    private int jwtRefresh;

    public JWTProvider() {
        // Generate a secure key for HS512
        this.jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(jwtSecret, SignatureAlgorithm.HS512) // Use the secure key
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token hết hạn!");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token không được hỗ trợ!");
        } catch (MalformedJwtException e) {
            log.error("JWT token không đúng định dạng!");
        } catch (SignatureException e) {
            log.error("Lỗi chữ ký JWT token!");
        } catch (IllegalArgumentException e) {
            log.error("Lỗi tham số JWT token!");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String refreshToken(String token, String username) {
        if (validateToken(token) && getUsernameFromToken(token).equals(username)) {
            Date now = new Date();
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + jwtRefresh))
                    .signWith(jwtSecret, SignatureAlgorithm.HS512) // Use the secure key
                    .compact();
        }
        return null;
    }

    public int getJwtExpire() {
        return jwtExpire;
    }
}