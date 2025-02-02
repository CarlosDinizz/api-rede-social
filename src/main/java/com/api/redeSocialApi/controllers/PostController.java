package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.PostRequestDTO;
import com.api.redeSocialApi.dtos.PostResponseCreatedDTO;
import com.api.redeSocialApi.dtos.PostResponseDTO;
import com.api.redeSocialApi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService service;

    @Autowired
    public PostController(PostService service){
        this.service = service;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<PostResponseCreatedDTO> newPost(@RequestBody PostRequestDTO requestDTO, @PathVariable String userId){
        PostResponseCreatedDTO responseDTO = service.newPost(requestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findPost(@PathVariable UUID id){
        PostResponseDTO postDTO = service.findPost(id);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PostResponseDTO>> postsByUserId(@PathVariable UUID userId){
        List<PostResponseDTO> dtoList = service.postsByUserId(userId);
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable UUID id,@RequestBody PostRequestDTO requestDTO){
        service.updatePost(id, requestDTO);
        return ResponseEntity.noContent().build();
    }
}
