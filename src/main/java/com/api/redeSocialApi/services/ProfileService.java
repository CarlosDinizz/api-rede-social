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
    private JwtService jwtService;

    @Autowired
    public ProfileService(ProfileRepository repository, FollowingRepository followingRepository, FollowersRepository followersRepository, JwtService jwtService) {
        this.repository = repository;
        this.followingRepository = followingRepository;
        this.followersRepository = followersRepository;
        this.jwtService = jwtService;
    }

    public void registerProfile(Profile profile){
        profile = repository.save(profile);
        followersRepository.save(new Followers(profile));
        followingRepository.save(new Following(profile));
    }

    public ProfileResponseDTO findProfile(UUID id){
        Optional<Profile> profile = repository.findById(id);
        if (profile.isEmpty()){
            throw new ProfileNotFoundException("Profile not found");
        }
        return toDto(profile.get());
    }

    public void updateProfile(UUID id, ProfileRequestDTO requestDTO, JwtAuthenticationToken token){
        Boolean usernameExists = repository.existsByUsername(requestDTO.username());

        if (usernameExists){
            throw new ProfileUsernameException("Username already exists");
        }

        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException(("Profile not found")));
        Boolean userAndTokenValid = jwtService.validateToken(profile.getUser(), token);

        if (!userAndTokenValid){
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
