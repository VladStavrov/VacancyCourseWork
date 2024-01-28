package com.example.apigateway.config;

import io.swagger.v3.oas.annotations.Operation;


import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class SwaggerUiConfig {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Operation(summary = "Update from API",
            description = "Update the database from an external API.")
        @GetMapping("/swagger-config.json")
        public Map<String, Object> swaggerConfig() {
            Map<String, Object> config = new LinkedHashMap<>();
            List<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new LinkedList<>();
            discoveryClient.getServices().forEach(serviceName ->
                    discoveryClient.getInstances(serviceName).forEach(serviceInstance ->
                            urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl( serviceName,"http://localhost:"+serviceInstance.getPort() + "/v3/api-docs",serviceName))
                    )
            );
            config.put("urls", urls);
            return config;
        }

    @GetMapping("/hi")
    public String gi(){
        return "Hello Vlad";
    }
}