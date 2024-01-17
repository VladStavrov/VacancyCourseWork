package com.example.treeservice.kafka.producer;


import com.example.treeservice.dto.NodeDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NodeEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void sendNodeUpdateNotification(NodeDTO nodeDTO){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(nodeDTO);
            System.out.println("---------------send : "+jsonString);
            kafkaTemplate.send("nodeUpdateTopic", jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendNodeDeleteNotification(String slug){

            System.out.println("---------------send : "+slug);
            kafkaTemplate.send("nodeDeleteTopic", slug);
    }
}