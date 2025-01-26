package com.api.redeSocialApi.services;

import com.api.redeSocialApi.repositories.ResponseId;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.dtos.PostDTO;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ResponseId newPost(PostDTO postDTO, String idUser) {
        User user = userRepository.findById(UUID.fromString(idUser)).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = new Post(postDTO, user);
        post = repository.save(post);
        return new ResponseId(post.getId());
    }

    public PostDTO findPost(UUID id) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not Found"));
        PostDTO postDTO = new PostDTO(post);
        return postDTO;
    }

    public void deletePost(UUID id){
        repository.deleteById(id);
    }

    public List<PostDTO> postsByUserId(UUID userId) {
        List<Post> posts = repository.findPostByUserId(userId);
        List<PostDTO> dtoList = posts.stream().map(PostDTO::new).toList();
        return dtoList;
    }

    public void updatePost(UUID id, PostDTO postDTO) {
        Post post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setDescription(postDTO.getDescription());

        if (postDTO.getUrlImg() != null){
            post.setUrlImg(postDTO.getUrlImg());
        }

        repository.save(post);
    }
}
