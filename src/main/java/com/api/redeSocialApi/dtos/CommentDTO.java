package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.Comment;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private UUID id;
    private String description;
    private Integer likes;
    private LocalDateTime time;
    private Post post;
    private User user;

    public CommentDTO(Comment comment){
        id = comment.getId();
        description = comment.getDescription();
        likes = comment.getLikes();
        time = comment.getTime();
        post = comment.getPost();
        user = comment.getUser();
    }
}
