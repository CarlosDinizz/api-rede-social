package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.CommentRequestDTO;
import com.api.redeSocialApi.dtos.CommentResponseDTO;
import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @RequestParam("user") UUID userId,
            @RequestBody CommentRequestDTO requestDTO
    ){
        CommentResponseDTO response = service.createComment(requestDTO, userId, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id){
        service.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}
