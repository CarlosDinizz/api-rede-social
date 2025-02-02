package com.api.redeSocialApi.services;

import com.api.redeSocialApi.dtos.PostRequestDTO;
import com.api.redeSocialApi.dtos.PostResponseCreatedDTO;
import com.api.redeSocialApi.dtos.PostResponseDTO;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private PostRepository repository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository repository, UserRepository userRepository){
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public PostResponseCreatedDTO newPost(PostRequestDTO requestDTO, String idUser) {
        User user = userRepository.findById(UUID.fromString(idUser)).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = toPost(requestDTO, user);
        post = repository.save(post);
        PostResponseCreatedDTO createdDTO = toPostCreatedDto(post);
        return createdDTO;
    }

    public PostResponseDTO findPost(UUID id) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not Found"));
        PostResponseDTO responseDTO = toDto(post);
        return responseDTO;
    }

    public void deletePost(UUID id){
        repository.deleteById(id);
    }

    public List<PostResponseDTO> postsByUserId(UUID userId) {
        List<Post> posts = repository.findPostByUserId(userId);
        List<PostResponseDTO> dtoList = posts.stream().map(this::toDto).toList();
        return dtoList;
    }

    public void updatePost(UUID id, PostRequestDTO requestDTO) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setDescription(requestDTO.description());

        if (requestDTO.img() != null){
            post.setUrlImg(requestDTO.img());
        }

        repository.save(post);
    }

    public PostResponseDTO toDto(Post post){
        return new PostResponseDTO(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getUrlImg(),
                post.getDescription(),
                post.getLikes(),
                post.getTime(),
                post.getComments()
        );
    }

    public PostResponseCreatedDTO toPostCreatedDto(Post post){
        return new PostResponseCreatedDTO(
                post.getId(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getUrlImg(),
                post.getDescription(),
                post.getLikes(),
                post.getTime()
        );
    }

    public Post toPost(PostRequestDTO requestDTO, User user){
        Post post = new Post();
        post.setUrlImg(requestDTO.img());
        post.setDescription(requestDTO.description());
        post.setLikes(0);
        post.setTime(LocalDateTime.now());
        post.setIsCommentsBlocked(requestDTO.is_comments_blocked());
        post.setUser(user);

        return post;
    }
}
