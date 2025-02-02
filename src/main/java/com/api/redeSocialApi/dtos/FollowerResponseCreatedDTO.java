package com.api.redeSocialApi.dtos;


import java.util.UUID;

public record FollowerResponseCreatedDTO(UUID id, String user, String follower){}
