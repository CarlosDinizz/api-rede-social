package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.PostRequestDTO;
import com.api.redeSocialApi.dtos.PostResponseCreatedDTO;
import com.api.redeSocialApi.dtos.PostResponseDTO;
import com.api.redeSocialApi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    @PostMapping("/{profileId}")
    public ResponseEntity<PostResponseCreatedDTO> newPost(
            @RequestBody PostRequestDTO requestDTO,
            @PathVariable UUID profileId,
            JwtAuthenticationToken token
    ){
        PostResponseCreatedDTO responseDTO = service.newPost(requestDTO, profileId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findPost(@PathVariable UUID id){
        PostResponseDTO postDTO = service.findPost(id);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> postsByUserId(@RequestParam(name = "profileId") UUID profileId){
        List<PostResponseDTO> dtoList = service.postsByProfileId(profileId);
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, JwtAuthenticationToken token){
        service.deletePost(id, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable UUID id,
            @RequestBody PostRequestDTO requestDTO,
            JwtAuthenticationToken token
    ){
        service.updatePost(id, requestDTO, token);
        return ResponseEntity.noContent().build();
    }
}
