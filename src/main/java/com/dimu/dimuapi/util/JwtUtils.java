package com.dimu.dimuapi.util;

import com.dimu.dimuapi.constant.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret; // Remove static

    private static JwtUtils instance;



    @PostConstruct
    public void init() {
        instance = this;
    }

    public static String generateToken(String email, String roles) {
            SecretKey secretKey = Keys.hmacShaKeyFor(instance.jwtSecret.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().issuer("Diimu")
                    .claim("email", email)
                    .claim("roles", roles)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30 ))
                    .signWith(secretKey).compact();

            return jwt;
    }

    public static Claims validateToken(String jwt) {
        SecretKey secretKey = Keys.hmacShaKeyFor(instance.jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(jwt).getPayload();
    }
}
