package com.dimu.dimuapi.filter;

import com.dimu.dimuapi.constant.ApplicationConstants;
import com.dimu.dimuapi.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtValidationFilter extends OncePerRequestFilter {
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String jwt = request.getHeader(ApplicationConstants.JWT_HEADER).split(" ")[1];
    if(jwt != null){
        try{
          Claims claims = JwtUtils.validateToken(jwt);
          String email = claims.get("email").toString();
          String roles = claims.get("roles").toString();

            Authentication authentication = new UsernamePasswordAuthenticationToken(email,null, AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }

    }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equalsIgnoreCase("/auth/login")
                ||!request.getServletPath().contains("api/");
    }
}
