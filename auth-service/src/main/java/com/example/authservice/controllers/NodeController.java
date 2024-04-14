package com.example.authservice.controllers;


import com.example.authservice.DTOs.profile.node.NodeCreateDTO;
import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.services.profile.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    private final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodes = nodeService.getAllNodes();
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<NodeDTO> getNodeBySlug(@PathVariable String slug) {
        NodeDTO node = nodeService.getNodeDTOBySlug(slug);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeCreateDTO nodeDTO) {
        NodeDTO createdNode = nodeService.createNode(nodeDTO);
        return new ResponseEntity<>(createdNode, HttpStatus.CREATED);
    }
    @PutMapping("/{slug}")
    public ResponseEntity<NodeDTO> updateNode(@PathVariable String slug, @RequestBody NodeDTO updatedNodeDTO) {
        NodeDTO updatedNode = nodeService.updateNode(slug, updatedNodeDTO);
        return new ResponseEntity<>(updatedNode, HttpStatus.OK);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteNode(@PathVariable String slug) {
        nodeService.deleteNode(slug);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}