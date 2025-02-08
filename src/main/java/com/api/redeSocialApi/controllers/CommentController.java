package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.CommentRequestDTO;
import com.api.redeSocialApi.dtos.CommentResponseDTO;
import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

   private CommentService service;

   @Autowired
   public CommentController(CommentService service){
       this.service = service;
   }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @RequestParam("post") UUID postId,
            @RequestParam("userProfile") UUID profileId,
            @RequestBody CommentRequestDTO requestDTO,
            JwtAuthenticationToken token
    ){
        CommentResponseDTO response = service.createComment(requestDTO, profileId, postId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id, JwtAuthenticationToken token){
        service.deleteCommentById(id, token);
        return ResponseEntity.noContent().build();
    }
}
