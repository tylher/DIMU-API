package com.dimu.dimuapi.filter;

import com.dimu.dimuapi.constant.ApplicationConstants;
import com.dimu.dimuapi.util.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


public class JwtGenerationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String jwt = JwtUtils.generateToken(authentication.getName(),
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")));

            response.addHeader(ApplicationConstants.JWT_HEADER, "Bearer " + jwt);
        }
        filterChain.doFilter(request,response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/auth/login");
    }
}
