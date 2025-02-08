package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.FollowingResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowingService {

    private FollowingRepository repository;
    private ProfileRepository profileRepository;
    private FollowersRepository followersRepository;

    @Autowired
    public FollowingService(FollowingRepository repository, ProfileRepository profileRepository, FollowersRepository followersRepository){
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.followersRepository = followersRepository;
    }

    public FollowingResponseDTO findFollowing(UUID id) {
        Following following =  repository.findByProfileId(id);
        return toDto(following);
    }

    public void deleteFollowingByProfileId(UUID profile, UUID following, JwtAuthenticationToken token){
        Profile theProfile = profileRepository.findById(profile).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Profile theFollowing = profileRepository.findById(following).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        verifyToken(theProfile, token);

        Following followingEntity = theProfile.getFollowingEntity();
        if (!followingEntity.getFollowing().contains(theFollowing)){
            throw new RuntimeException();
        }
        followingEntity.getFollowing().remove(theFollowing);
        repository.save(followingEntity);

        Followers theFollowerFromTheFollowing = theFollowing.getFollowerEntity();
        theFollowerFromTheFollowing.getFollowers().remove(theProfile);
        followersRepository.save(theFollowerFromTheFollowing);
    }

    public FollowingResponseDTO toDto(Following following){
        return new FollowingResponseDTO(
                following.getId(),
                following.getProfile().getId(),
                following.getProfile().getUsername(),
                following.getFollowing()
        );
    }

    private void verifyToken(Profile profile, JwtAuthenticationToken token){
        if (!profile.getUser().getEmail().equals(token.getName())){
            throw new UserUnauthorizedException();
        }

    }
}
