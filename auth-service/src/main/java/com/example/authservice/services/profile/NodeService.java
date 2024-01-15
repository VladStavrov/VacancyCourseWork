package com.example.authservice.services.profile;



import com.example.authservice.DTOs.profile.NodeCreateDTO;
import com.example.authservice.DTOs.profile.NodeDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.repositories.profile.NodeRepository;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final ModelMapper modelMapper;
    private final NodeRepository nodeRepository;

    public List<NodeDTO> getAllNodes() {
        List<Node> nodes = nodeRepository.findAll();
        return nodes.stream()
                .map(this::mapNodeToDTO)
                .collect(Collectors.toList());
    }
    public NodeDTO getNodeDTOBySlug(String slug) {
        Node node = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));

        return mapNodeToDTO(node);
    }
    public Node getNodeBySlug(String slug) {
        Node node = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));

        return node;
    }


    public Node createNode(NodeDTO nodeDTO) {
        NodeCreateDTO nodeCreateDTO = mapDTOToNodeCreateDTO(nodeDTO);

        Node node = mapDTOToNode(nodeCreateDTO);

        Node createdNode = nodeRepository.save(node);
        return createdNode;
    }
    public NodeDTO updateNode(String slug, NodeDTO updatedNodeDTO) {
        Node existingNode = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));

        if (updatedNodeDTO.getTitle() != null) {
            existingNode.setTitle(updatedNodeDTO.getTitle());
        }
        if (updatedNodeDTO.getSlug() != null) {
            existingNode.setSlug(updatedNodeDTO.getSlug());
        }
        Node updatedNode = nodeRepository.save(existingNode);

        return mapNodeToDTO(updatedNode);
    }

    @Transactional
    public void deleteNode(String slug) {
        Node node = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));


        nodeRepository.delete(node);
    }
   /* @Transactional
    public void deleteNode(String slug) {
        Node node = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));


        if (node != null) {
            // Очистить отношения к primarySkillExperiences и secondarySkillExperiences
            node.getPrimarySkillExperiences().clear();
            node.getSecondarySkillExperiences().clear();

            // Сохранить изменения в базе данных
            nodeRepository.save(node);
        }
    }*/


    public NodeDTO mapNodeToDTO(Node node) {
        return modelMapper.map(node, NodeDTO.class);
    }
    public Node mapDTOToNode(NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }

    public NodeCreateDTO mapDTOToNodeCreateDTO(NodeDTO nodeDTO) {
        return modelMapper.map(nodeDTO, NodeCreateDTO.class);
    }
    public Node mapDTOToNode(NodeCreateDTO nodeDTO) {
        return modelMapper.map(nodeDTO, Node.class);
    }

}
