package com.api.redeSocialApi.dtos;

import java.util.UUID;

public record RegisterLoginResponseDTO(String token, UUID profile_id) {
}
