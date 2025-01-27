package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.Comment;
import com.api.redeSocialApi.domain.Post;
import com.api.redeSocialApi.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private UUID id;
    private String urlImg;
    private String description;
    private Integer likes;
    private LocalDateTime time;
    private Boolean isCommentsBlocked;
    private User user;
    private List<Comment> comments;

    public PostDTO(Post post){
        id = post.getId();
        urlImg = post.getUrlImg();
        description = post.getDescription();
        likes = post.getLikes();
        time = post.getTime();
        isCommentsBlocked = post.getIsCommentsBlocked();
        user = post.getUser();
        comments = post.getComments();
    }
}
