package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.domain.exceptions.UserNotFoundException;
import com.api.redeSocialApi.dtos.RegisterRequestDTO;
import com.api.redeSocialApi.dtos.UserResponseCreatedDTO;
import com.api.redeSocialApi.dtos.UserResponseDTO;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public UserResponseDTO findUser(UUID id){
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Not found"));
        UserResponseDTO response = toResponseDto(user);
        return response;
    }

    public UserResponseDTO toResponseDto(User user){
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile().getId());
    }
}
