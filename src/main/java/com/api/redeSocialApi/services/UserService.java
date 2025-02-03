package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.dtos.UserRequestCreatedDTO;
import com.api.redeSocialApi.dtos.UserResponseCreatedDTO;
import com.api.redeSocialApi.dtos.UserResponseDTO;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository repository;
    private ProfileService profileService;

    @Autowired
    public UserService(UserRepository repository, ProfileService profileService){
        this.repository = repository;
        this.profileService = profileService;

    }

    public UserResponseDTO findUser(UUID id){
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        UserResponseDTO response = toResponseDto(user);
        return response;
    }

    public UserResponseCreatedDTO registerUser(UserRequestCreatedDTO request){
        User user = toUserThroughRequestCreated(request);
        user = repository.save(user);

        profileService.registerProfile(new Profile(request.username(), user));
        return toResponseCreatedDto(user);
    }

    public User toUserThroughRequestCreated(UserRequestCreatedDTO request){
        User user = new User();
        user.setFirstName(request.first_name());
        user.setLastName(request.last_name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setIs_enabled(true);
        return user;
    }


    public UserResponseCreatedDTO toResponseCreatedDto(User user){
        return new UserResponseCreatedDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public UserResponseDTO toResponseDto(User user){
        return new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile().getId());
    }
}
