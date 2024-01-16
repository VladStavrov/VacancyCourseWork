package com.example.apigateway.utils;



import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.List;


@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    public void validateToken(final String token) {
        System.out.println(3.5);
        Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token);
    }
    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles",List.class);
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    }