package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.Post;

import java.time.LocalDateTime;
import java.util.UUID;


public record CommentResponseDTO(
        UUID id,
        UUID profile_id,
        String username,
        String description,
        Integer likes,
        LocalDateTime time,
        Post post
){}
