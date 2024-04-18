package com.example.authservice.DTOs.profile.node;

import com.example.authservice.models.profile.Node;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodeDTO {
    private Long id;
    private String nodeType;
    private String title;
    private String slug;
    private String content;
    public NodeDTO(Node node) {
        this.id = node.getId();
        this.nodeType = node.getNodeType();
        this.title = node.getTitle();
        this.slug = node.getSlug();
        this.content = node.getContent();
    }
}
