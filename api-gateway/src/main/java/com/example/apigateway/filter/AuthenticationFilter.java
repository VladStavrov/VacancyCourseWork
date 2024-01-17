package com.example.apigateway.filter;



import com.example.apigateway.exceptions.LocalException;
import com.example.apigateway.utils.JWTUtil;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
        System.out.println("как");
        return ((exchange, chain) -> {
            System.out.println(1);
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new LocalException(HttpStatus.UNAUTHORIZED, "Un authorized access to application");
            }
            System.out.println(2);
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            ServerHttpRequest request;
            try {

                System.out.println(authHeader);
                jwtUtil.validateToken(authHeader);
                System.out.println(4);
                String requiredRole = config.requiredRole;
                if(requiredRole==null){
                    requiredRole=DEFAULT_REQUIRED_ROLE;
                }

                    List<String> roles = jwtUtil.getRoles(authHeader);
                System.out.println("roles: "+roles);
                    if (!isValidRole(roles,requiredRole)) {

                        throw new LocalException(HttpStatus.FORBIDDEN, "User does not have the required role");
                    }
                System.out.println("Мы дошли");
                    request=exchange.getRequest()
                            .mutate()
                            .header("loadedUsername",jwtUtil.getUsername(authHeader))
                            .build();

            } catch (Exception e) {
                throw new LocalException(HttpStatus.UNAUTHORIZED, "Un authorized access to application");
            }

            return chain.filter(exchange.mutate().request(request).build());
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