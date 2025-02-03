package com.api.redeSocialApi.dtos;

import java.util.UUID;

public record UserResponseCreatedDTO(
        UUID id,
        String first_name,
        String last_name,
        String email
) {}
