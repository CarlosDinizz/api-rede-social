package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.ProfileUsernameException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.ProfileRequestDTO;
import com.api.redeSocialApi.dtos.ProfileResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private ProfileRepository repository;
    private FollowingRepository followingRepository;
    private FollowersRepository followersRepository;

    @Autowired
    public ProfileService(ProfileRepository repository, FollowingRepository followingRepository, FollowersRepository followersRepository) {
        this.repository = repository;
        this.followingRepository = followingRepository;
        this.followersRepository = followersRepository;
    }

    public void registerProfile(Profile profile){
        profile.setBio("");
        profile = repository.save(profile);
        followersRepository.save(new Followers(profile));
        followingRepository.save(new Following(profile));
    }

    public ProfileResponseDTO findProfile(UUID id){
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return toDto(profile);
    }

    public void updateProfile(UUID id, ProfileRequestDTO requestDTO, JwtAuthenticationToken token){
        Optional<Profile> usernameExists = repository.findByUsername(requestDTO.username());

        if (usernameExists.isPresent()){
            throw new ProfileUsernameException("Username already exists");
        }

        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException(("Profile not found")));

        if (!profile.getUser().getEmail().equals(token.getName())){
            throw new UserUnauthorizedException();
        }

        profile.setUsername(requestDTO.username());

        if (requestDTO.bio() != null){
            profile.setBio(requestDTO.bio());
        }

        repository.save(profile);
    }

    public ProfileResponseDTO toDto(Profile profile){
        ProfileResponseDTO responseDTO = new ProfileResponseDTO(profile.getId(), profile.getUsername(), profile.getBio(), profile.getFollowerEntity().getFollowers().size(), profile.getFollowingEntity().getFollowing().size());
        return responseDTO;
    }

}
