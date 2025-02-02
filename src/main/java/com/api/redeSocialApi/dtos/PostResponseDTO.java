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


public record PostResponseDTO(
        UUID id,
        UUID user_id,
        String username,
        String img,
        String description,
        Integer likes,
        LocalDateTime time,
        List<Comment> comments
){}
