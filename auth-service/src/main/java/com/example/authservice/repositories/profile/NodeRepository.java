package com.example.authservice.repositories.profile;



import com.example.authservice.models.profile.Node;
import org.reactivestreams.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface NodeRepository extends JpaRepository<Node,Long> {



    Optional<Node> findBySlug(String slug);
}
