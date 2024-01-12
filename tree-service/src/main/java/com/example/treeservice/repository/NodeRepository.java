package com.example.treeservice.repository;

import com.example.treeservice.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface NodeRepository extends JpaRepository<Node,Long> {

    Optional<Node> findBySlug(String slug);
}
