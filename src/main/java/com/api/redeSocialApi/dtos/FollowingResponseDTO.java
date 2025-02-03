package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.Profile;


import java.util.List;
import java.util.UUID;

public record FollowingResponseDTO(UUID id, UUID profile_id, String username, List<Profile> following) {
}
