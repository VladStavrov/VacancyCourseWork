package com.example.authservice.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WevClientConfig {
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}