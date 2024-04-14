/*
package com.example.authservice.kafka.consumer;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.services.profile.NodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeEventConsumer {

    private final NodeService nodeService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "nodeDeleteTopic")
    public void nodeDeleteEvent(String slug) {

        System.out.println("delete-----------------------------------" +  slug + " ----------------------------------------------------------------------------");
        nodeService.deleteNode(slug);
    }
    @KafkaListener(topics = "nodeUpdateTopic")
    public void nodeUpdateEvent(String message) {
        NodeDTO nodeDTO = null;
        try {
            nodeDTO = objectMapper.readValue(message, NodeDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("update-----------------------------------" +  nodeDTO + " ----------------------------------------------------------------------------");
        nodeService.updateNode(nodeDTO.getSlug(),nodeDTO);
    }
}*/
