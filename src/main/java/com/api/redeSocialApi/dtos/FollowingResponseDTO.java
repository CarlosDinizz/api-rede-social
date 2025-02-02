package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.User;

import java.util.List;
import java.util.UUID;

public record FollowingResponseDTO(UUID id, UUID user_id, String username, List<User> following) {
}
