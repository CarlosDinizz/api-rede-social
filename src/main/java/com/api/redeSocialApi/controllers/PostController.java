package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.dtos.PostDTO;
import com.api.redeSocialApi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseId> newPost(@RequestBody PostDTO postDTO, @PathVariable String userId){
        ResponseId id = service.newPost(postDTO, userId);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findPost(@PathVariable UUID id){
        PostDTO postDTO = service.findPost(id);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PostDTO>> postsByUserId(@PathVariable UUID userId){
        List<PostDTO> dtoList = service.postsByUserId(userId);
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable UUID id){
        service.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable UUID id,@RequestBody PostDTO postDTO){
        service.updatePost(id, postDTO);
        return ResponseEntity.ok().build();
    }
}
