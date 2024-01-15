package com.example.treeservice.controller;

import com.example.treeservice.dto.NodeCreateDTO;
import com.example.treeservice.dto.NodeDTO;
import com.example.treeservice.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nodes")
public class NodeController {
    private final NodeService nodeService;
    @GetMapping
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodes = nodeService.getAllNodes();
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }
    @GetMapping("/{slug}")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable String slug) {
        NodeDTO nodeDTO = nodeService.getNodeDTOBySlug(slug);
        return new ResponseEntity<>(nodeDTO, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeCreateDTO nodeCreateDTO) {
        NodeDTO createdNode = nodeService.createNode(nodeCreateDTO);
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
    @GetMapping("/updateApi")
    public ResponseEntity<?> updateFromApi(@RequestParam("token") String token){
        nodeService.updateDBFromApi(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
