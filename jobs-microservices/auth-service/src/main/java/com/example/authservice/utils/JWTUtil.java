package com.example.authservice.utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private final String secret= "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    private final Duration lifetime=Duration.ofMinutes(60L);
    private final Duration lifetime2=Duration.ofMinutes(50L);
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() +lifetime2.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }
    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime()+ lifetime.toMillis());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }
    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }






}
