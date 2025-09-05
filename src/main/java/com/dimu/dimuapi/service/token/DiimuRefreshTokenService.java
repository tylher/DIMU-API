package com.dimu.dimuapi.service.token;

import com.dimu.dimuapi.Enum.TokenFormat;
import com.dimu.dimuapi.Enum.TokenType;
import com.dimu.dimuapi.model.DiimuToken;
import com.dimu.dimuapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DiimuRefreshTokenService{
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    final String PREFIX = "refresh-token:";

    public String createRefreshToken(User user){
        String token  = DiimuToken.generateRandomToken(TokenType.REFRESH, TokenFormat.UUID);
        redisTemplate.opsForValue().set(PREFIX+token,user.getUserId()
                , Duration.ofMinutes(TokenType.REFRESH.getExpiry()));
        return token;
    }

    public boolean isTokenValid(String token){
       return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token));
    }

    public String getUserId(String token){
        return redisTemplate.opsForValue().get(PREFIX+token);
    }

    public void deleteToken(String token){
        redisTemplate.delete(PREFIX+token);
    }
}
