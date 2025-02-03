package com.api.redeSocialApi.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseCreatedDTO(
        UUID id,
        UUID profile_id,
        String username,
        String img,
        String description,
        Integer likes,
        LocalDateTime time
) {
}
