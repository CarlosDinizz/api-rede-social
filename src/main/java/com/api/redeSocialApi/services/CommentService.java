package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Comment;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.dtos.CommentDTO;
import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.repositories.CommentRepository;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommentService {

    private CommentRepository repository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository repository, PostRepository postRepository, UserRepository userRepository){
        this.repository = repository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseId createComment(CommentDTO commentDTO, UUID userId, UUID postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment(commentDTO, user, post);
        comment = repository.save(comment);
        return new ResponseId(comment.getId());
    }

    public void deleteCommentById(UUID id) {
        repository.deleteById(id);
    }
}
