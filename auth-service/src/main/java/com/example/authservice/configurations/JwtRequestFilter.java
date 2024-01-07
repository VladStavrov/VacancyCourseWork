package com.example.authservice.configurations;

import com.example.authservice.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt=parseJwtFromHeader(authHeader);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                try{

                    String username = jwtUtil.getUsername(jwt);
                    if (StringUtils.isNotEmpty(username)) {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                username,
                                null
                        );
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
                catch(ExpiredJwtException e){
                    log.debug("Token lifetime has expired");
                } catch (SignatureException e){
                    log.debug("The signature is incorrect");
                }
            } catch (Exception e) {
                log.debug("Failed to authenticate user");
            }
        }

        filterChain.doFilter(request,response);
    }


    private String parseJwtFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}
