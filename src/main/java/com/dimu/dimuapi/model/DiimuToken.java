package com.dimu.dimuapi.model;

import com.dimu.dimuapi.Enum.TokenFormat;
import com.dimu.dimuapi.Enum.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.dimu.dimuapi.constant.ApplicationConstants.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiimuToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean used;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static String generateRandomToken(TokenType tokenType,TokenFormat tokenFormat) {

        // Special case: use UUID for refresh tokens
        if (tokenType == TokenType.REFRESH) {
            return UUID.randomUUID().toString().replace("-", ""); // 32 chars without hyphens
        }

        SecureRandom random = new SecureRandom();
        String characters;
        StringBuilder str = new StringBuilder(tokenType.getLength());

        switch (tokenFormat){
            case NUMERIC -> characters=NUMERIC_CHARS;
            case ALPHABETIC -> characters = ALPHABET_CHARS;
            case ALPHANUMERIC -> characters = ALPHANUMERIC_CHARS;
            default -> characters = "";
        }

        for (int i = 0; i < tokenType.getLength(); i++) {
            str.append(characters.charAt(random.nextInt(characters.length())));
        }
        return  str.toString();
    }


}
