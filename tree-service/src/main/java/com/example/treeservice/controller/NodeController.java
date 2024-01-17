package com.example.treeservice.controller;

import com.example.treeservice.dto.NodeCreateDTO;
import com.example.treeservice.dto.NodeDTO;
import com.example.treeservice.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Get all nodes",
            description = "Get a list of all nodes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NodeDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<NodeDTO>> getAllNodes() {
        List<NodeDTO> nodes = nodeService.getAllNodes();
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    @Operation(summary = "Get node by slug",
            description = "Get details of a specific node by its slug.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NodeDTO.class)))
    })
    @GetMapping("/{slug}")
    public ResponseEntity<NodeDTO> getNodeById(@PathVariable String slug) {
        NodeDTO nodeDTO = nodeService.getNodeDTOBySlug(slug);
        return new ResponseEntity<>(nodeDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create node",
            description = "Create a new node. Returns the created NodeDTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NodeDTO.class)))
    })
    @PostMapping
    public ResponseEntity<NodeDTO> createNode(@RequestBody NodeCreateDTO nodeCreateDTO) {
        NodeDTO createdNode = nodeService.createNode(nodeCreateDTO);
        return new ResponseEntity<>(createdNode, HttpStatus.CREATED);
    }

    @Operation(summary = "Update node",
            description = "Update details of a specific node by its slug. Returns the updated NodeDTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NodeDTO.class)))
    })
    @PutMapping("/{slug}")
    public ResponseEntity<NodeDTO> updateNode(@PathVariable String slug, @RequestBody NodeDTO updatedNodeDTO) {
        NodeDTO updatedNode = nodeService.updateNode(slug, updatedNodeDTO);
        return new ResponseEntity<>(updatedNode, HttpStatus.OK);
    }

    @Operation(summary = "Delete node",
            description = "Delete a specific node by its slug. Returns HttpStatus.NO_CONTENT on success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteNode(@PathVariable String slug) {
        nodeService.deleteNode(slug);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update from API",
            description = "Update the database from an external API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted")
    })
    @GetMapping("/updateApi")
    public ResponseEntity<?> updateFromApi(@RequestParam("token") String token) {
        nodeService.updateDBFromApi(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
