package com.api.redeSocialApi.dtos;

public record UserRequestCreatedDTO(
        String first_name,
        String last_name,
        String email,
        String password,
        String username
) {
}
