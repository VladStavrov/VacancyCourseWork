package com.example.authservice.services.profile;



import com.example.authservice.DTOs.profile.node.ApiNodeDto;
import com.example.authservice.DTOs.profile.node.ApiNodeListDTO;
import com.example.authservice.DTOs.profile.node.NodeCreateDTO;
import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.repositories.profile.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeService {
    private final String apiRoute="https://api.ch.anto.sh/api/nodes/?page_size=400";
    private final WebClient webClient;

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
    public NodeDTO createNode(NodeCreateDTO nodeCreateDTO) {
        Node node = mapCreateDTOToNode(nodeCreateDTO);
        Node createdNode = nodeRepository.save(node);
        return mapNodeToDTO(createdNode);
    }
    public NodeDTO updateNode(String slug, NodeDTO updatedNodeDTO) {
        Node existingNode = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));
        if (updatedNodeDTO.getNodeType() != null) {
            existingNode.setNodeType(updatedNodeDTO.getNodeType());
        }
        if (updatedNodeDTO.getTitle() != null) {
            existingNode.setTitle(updatedNodeDTO.getTitle());
        }
        if (updatedNodeDTO.getSlug() != null) {
            existingNode.setSlug(updatedNodeDTO.getSlug());
        }
        if (updatedNodeDTO.getContent() != null) {
            existingNode.setContent(updatedNodeDTO.getContent());
        }
        Node updatedNode = nodeRepository.save(existingNode);
        NodeDTO nodeDTO = mapNodeToDTO(updatedNode);


        return nodeDTO;
    }

    @Transactional
    public void deleteNode(String slug) {
        Node node = nodeRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with slug: " + slug));
        nodeRepository.delete(node);
    }

    public Node mapCreateDTOToNode(NodeCreateDTO nodeCreateDTO) {
        return modelMapper.map(nodeCreateDTO, Node.class);
    }

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
    public NodeDTO mapApiDTOToNodeDTO(ApiNodeDto apiNodeDto){
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setId(apiNodeDto.getId());
        nodeDTO.setNodeType(apiNodeDto.getNode_type());
        nodeDTO.setTitle(apiNodeDto.getTitle());
        nodeDTO.setSlug(apiNodeDto.getSlug());
        nodeDTO.setContent(apiNodeDto.getContent());
        return nodeDTO;
    }
    //TODO
    public void updateDBFromApi(String jwtToken) {
        ApiNodeListDTO apiNodeListDTO = getNodeListFromApi(jwtToken);

        List<NodeDTO> nodeDTOList = apiNodeListDTO.getResults().stream()
                .map(this::mapApiDTOToNodeDTO)
                .toList();
        List<Node> nodeList = new ArrayList<>();
        for (NodeDTO nodeDTO : nodeDTOList) {
            Optional<Node> existingNode = nodeRepository.findBySlug(nodeDTO.getSlug());
            Node existing;
            if (existingNode.isPresent()) {
                existing = existingNode.get();
                existing.setNodeType(nodeDTO.getNodeType());
                existing.setTitle(nodeDTO.getTitle());
                existing.setSlug(nodeDTO.getSlug());
                existing.setContent(nodeDTO.getContent());
            } else {
                existing = mapDTOToNode(nodeDTO);
            }
            nodeRepository.save(existing);
        }
        System.out.println(";");
    }


    private ApiNodeListDTO getNodeListFromApi(String jwtToken) {
        return webClient
                .get()
                .uri(apiRoute)
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(ApiNodeListDTO.class)
                .onErrorResume(error -> {
                    if (error instanceof WebClientResponseException) {
                        WebClientResponseException responseException = (WebClientResponseException) error;
                        HttpStatus statusCode = (HttpStatus) responseException.getStatusCode();
                        if (statusCode == HttpStatus.UNAUTHORIZED) {
                            // Выбрасываем исключение с указанным статусом и сообщением
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is not valid"));
                        }
                    }
                    return Mono.error(error);
                })
                .block();
    }
}
