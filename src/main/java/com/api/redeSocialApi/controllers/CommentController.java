package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.CommentDTO;
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
    public ResponseEntity<ResponseId> addComment(
            @RequestParam("post") UUID postId,
            @RequestParam("user") UUID userId,
            @RequestBody CommentDTO commentDTO
    ){
        ResponseId response = service.createComment(commentDTO, userId, postId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable UUID id){
        service.deleteCommentById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
