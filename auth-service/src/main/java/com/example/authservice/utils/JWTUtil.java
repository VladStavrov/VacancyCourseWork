package com.example.authservice.utils;


import com.example.authservice.models.auth.Person;
import com.example.authservice.models.auth.Role;
import io.jsonwebtoken.*;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private  String secret="413F4428472B4B6250655368566D5970337336763979244226452948404D6351";

    @Value("${jwt.lifetime}")
    private  Long lifetime;
    public String generateToken(Person person ) throws ServletException {
        try {
        Map<String,Object> claims = new HashMap<>();
        List<String> rolesList= person.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        claims.put("roles",rolesList);
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() +Duration.ofMinutes(lifetime).toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(person.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    } catch (Exception e) {
        throw new ServletException("Error creating JWT token", e);
    }
    }

    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
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






}
