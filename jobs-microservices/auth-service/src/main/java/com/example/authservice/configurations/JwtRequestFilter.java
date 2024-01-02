package com.example.authservice.configurations;

import com.example.authservice.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            String username=null;
            String jwt=null;

            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                jwt=authHeader.substring(7);
                try{
                    username= jwtUtil.getUsername(jwt);
                }
                catch(ExpiredJwtException e){
                    log.debug("Jwt Expired");
                } catch (SignatureException e){
                    log.debug("Signature wrong");
                }
            }
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        username,
                        null
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            filterChain.doFilter(request,response);
    }
}