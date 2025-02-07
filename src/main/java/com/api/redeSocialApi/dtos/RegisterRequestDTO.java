package com.api.redeSocialApi.dtos;

public record RegisterRequestDTO(
        String first_name,
        String last_name,
        String email,
        String password,
        String username
) {
}
