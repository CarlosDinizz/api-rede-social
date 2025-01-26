package com.api.redeSocialApi.repositories;

import com.api.redeSocialApi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    public List<Post> findPostByUserId(UUID id);
}
