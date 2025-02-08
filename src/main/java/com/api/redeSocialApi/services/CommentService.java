package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Comment;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.exceptions.CommentNotFoundException;
import com.api.redeSocialApi.domain.exceptions.PostNotFoundException;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.CommentRequestDTO;
import com.api.redeSocialApi.dtos.CommentResponseDTO;
import com.api.redeSocialApi.repositories.CommentRepository;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CommentService {

    private CommentRepository repository;
    private ProfileRepository profileRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository repository, PostRepository postRepository, ProfileRepository profileRepository){
        this.repository = repository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    public CommentResponseDTO createComment(CommentRequestDTO requestDTO, UUID userId, UUID postId, JwtAuthenticationToken token) {
        Profile profile = profileRepository.findById(userId).orElseThrow(() -> new ProfileNotFoundException("User not found"));
        verifyToken(profile, token);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        Comment comment = toComment(requestDTO, profile, post);
        comment = repository.save(comment);
        return toResponseDto(comment);
    }

    public void deleteCommentById(UUID id, JwtAuthenticationToken token) {
        Comment comment = repository.findById(id).orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        System.out.println(isAuthorizeToDelete(comment,token));
        System.out.println(comment.getProfile().getUser().getEmail());
        System.out.println(comment.getPost().getProfile().getUser().getEmail());
        if (!isAuthorizeToDelete(comment, token)){
            throw new UserUnauthorizedException();
        }

        repository.deleteById(id);
    }

    public Comment toComment(CommentRequestDTO requestDTO, Profile profile, Post post){
        Comment comment = new Comment();
        comment.setDescription(requestDTO.description());
        comment.setProfile(profile);
        comment.setPost(post);
        comment.setTime(LocalDateTime.now());
        comment.setLikes(0);

        return  comment;
    }

    public CommentResponseDTO toResponseDto(Comment comment){
        return  new CommentResponseDTO(comment.getId(),
                comment.getProfile().getId(),
                comment.getProfile().getUsername(),
                comment.getDescription(),
                comment.getLikes(),
                comment.getTime(),
                comment.getPost()
        );
    }

    private Boolean isAuthorizeToDelete(Comment comment, JwtAuthenticationToken token){
        if (comment.getPost().getProfile().getUser().getEmail().equals(token.getName())){
            return true;
        }
        if (comment.getProfile().getUser().getEmail().equals(token.getName())){
            return true;
        }
        return false;
    }

    private void verifyToken(Profile profile, JwtAuthenticationToken token){
        if (!profile.getUser().getEmail().equals(token.getName())){
            throw new UserUnauthorizedException();
        }
    }
}
