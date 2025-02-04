package com.api.redeSocialApi.dtos;


import java.util.UUID;

public record ProfileResponseDTO(UUID id, String username, String bio, Integer followers, Integer following) {
}
