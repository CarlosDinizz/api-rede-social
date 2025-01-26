package com.api.redeSocialApi.domain;

import com.api.redeSocialApi.dtos.PostDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "url_image")
    private String urlImg;

    @Column(name = "description")
    private String description;

    @Column(name = "likes", nullable = false)
    private Integer likes;

    @Column(name = "time_recorded", nullable = false)
    private LocalDateTime time;

    @Column(name = "is_comments_blocked", nullable = false)
    private Boolean isCommentsBlocked;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private User user;

    public Post(PostDTO postDTO, User user){
        urlImg = postDTO.getUrlImg();
        description = postDTO.getDescription();
        likes = 0;
        time = LocalDateTime.now();
        isCommentsBlocked = postDTO.getIsCommentsBlocked();
        this.user = user;
    }
}
