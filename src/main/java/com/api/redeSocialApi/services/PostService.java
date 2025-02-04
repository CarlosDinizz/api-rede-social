package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.exceptions.PostNotFoundException;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.dtos.PostRequestDTO;
import com.api.redeSocialApi.dtos.PostResponseCreatedDTO;
import com.api.redeSocialApi.dtos.PostResponseDTO;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private PostRepository repository;
    private ProfileRepository profileRepository;

    @Autowired
    public PostService(PostRepository repository, ProfileRepository profileRepository){
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    public PostResponseCreatedDTO newPost(PostRequestDTO requestDTO, String idProfile) {
        Profile profile = profileRepository.findById(UUID.fromString(idProfile)).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Post post = toPost(requestDTO, profile);
        post = repository.save(post);
        PostResponseCreatedDTO createdDTO = toPostCreatedDto(post);
        return createdDTO;
    }

    public PostResponseDTO findPost(UUID id) {
        Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not Found"));
        PostResponseDTO responseDTO = toDto(post);
        return responseDTO;
    }

    public void deletePost(UUID id){
        Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        repository.deleteById(id);
    }

    public List<PostResponseDTO> postsByProfileId(UUID profileId) {
        List<Post> posts = repository.findPostByProfileId(profileId);
        if (posts.isEmpty()){
            throw new PostNotFoundException("Post not found");
        }
        List<PostResponseDTO> dtoList = posts.stream().map(this::toDto).toList();
        return dtoList;
    }

    public void updatePost(UUID id, PostRequestDTO requestDTO) {
        Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        post.setDescription(requestDTO.description());

        if (requestDTO.img() != null){
            post.setImg(requestDTO.img());
        }

        repository.save(post);
    }

    public PostResponseDTO toDto(Post post){
        return new PostResponseDTO(
                post.getId(),
                post.getProfile().getId(),
                post.getProfile().getUsername(),
                post.getImg(),
                post.getDescription(),
                post.getLikes(),
                post.getTime(),
                post.getComments()
        );
    }

    public PostResponseCreatedDTO toPostCreatedDto(Post post){
        return new PostResponseCreatedDTO(
                post.getId(),
                post.getProfile().getId(),
                post.getProfile().getUsername(),
                post.getImg(),
                post.getDescription(),
                post.getLikes(),
                post.getTime()
        );
    }

    public Post toPost(PostRequestDTO requestDTO, Profile profile){
        Post post = new Post();
        post.setImg(requestDTO.img());
        post.setDescription(requestDTO.description());
        post.setLikes(0);
        post.setTime(LocalDateTime.now());
        post.setIsCommentsBlocked(requestDTO.is_comments_blocked());
        post.setProfile(profile);

        return post;
    }
}
