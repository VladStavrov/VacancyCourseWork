package com.example.apigateway.filter;



import com.example.apigateway.exceptions.LocalException;
import com.example.apigateway.utils.JWTUtil;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private static final String DEFAULT_REQUIRED_ROLE = "ROLE_USER";

    private final JWTUtil jwtUtil;




    public AuthenticationFilter(JWTUtil jwtUtil) {
        super(Config.class);

        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("как так");
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new LocalException(HttpStatus.UNAUTHORIZED, "Un authorized access to application");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
                jwtUtil.validateToken(authHeader);
                String requiredRole = config.requiredRole;
                if(requiredRole==null){
                    requiredRole=DEFAULT_REQUIRED_ROLE;
                }
                    List<String> roles = jwtUtil.getRoles(authHeader);
                    if (!isValidRole(roles,requiredRole)) {

                        throw new LocalException(HttpStatus.FORBIDDEN, "User does not have the required role");
                    }

            } catch (Exception e) {
                throw new LocalException(HttpStatus.UNAUTHORIZED, "Un authorized access to application");
            }

            return chain.filter(exchange);
        });
    }

    private boolean isValidRole(List<String> roles,String requiredRole) {

        return roles.contains(requiredRole);
    }

    @Data
    public static class Config {
        private String requiredRole;

    }

}