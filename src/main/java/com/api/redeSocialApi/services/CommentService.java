package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Comment;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.dtos.CommentRequestDTO;
import com.api.redeSocialApi.dtos.CommentResponseDTO;
import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.repositories.CommentRepository;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public CommentResponseDTO createComment(CommentRequestDTO requestDTO, UUID userId, UUID postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = toComment(requestDTO, user, post);
        comment = repository.save(comment);
        return toResponseDto(comment);
    }

    public void deleteCommentById(UUID id) {
        repository.deleteById(id);
    }

    public Comment toComment(CommentRequestDTO requestDTO, User user, Post post){
        Comment comment = new Comment();
        comment.setDescription(requestDTO.description());
        comment.setUser(user);
        comment.setPost(post);
        comment.setTime(LocalDateTime.now());
        comment.setLikes(0);

        return  comment;
    }

    public CommentResponseDTO toResponseDto(Comment comment){
        return  new CommentResponseDTO(comment.getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getDescription(),
                comment.getLikes(),
                comment.getTime(),
                comment.getPost()
        );
    }
}
