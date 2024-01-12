package com.example.treeservice.controller;


import com.example.treeservice.dto.NodeCreateDTO;
import com.example.treeservice.dto.NodeDTO;
import com.example.treeservice.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @GetMapping("/{id}")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable Long id) {
        NodeDTO nodeDTO = nodeService.getNodeDTOById(id);
        return new ResponseEntity<>(nodeDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeCreateDTO nodeCreateDTO) {
        NodeDTO createdNode = nodeService.createNode(nodeCreateDTO);
        return new ResponseEntity<>(createdNode, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeDTO> updateNode(@PathVariable Long id, @RequestBody NodeDTO updatedNodeDTO) {
        NodeDTO updatedNode = nodeService.updateNode(id, updatedNodeDTO);
        return new ResponseEntity<>(updatedNode, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable Long id) {
        nodeService.deleteNode(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/updateApi")
    public ResponseEntity<?> updateFromApi(@RequestParam("token") String token){
        nodeService.updateDBFromApi(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
