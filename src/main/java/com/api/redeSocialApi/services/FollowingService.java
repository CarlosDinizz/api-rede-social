package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.exceptions.FollowingException;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.FollowingResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FollowingService {

    private FollowingRepository repository;
    private ProfileRepository profileRepository;
    private FollowersRepository followersRepository;
    private JwtService jwtService;

    @Autowired
    public FollowingService(FollowingRepository repository, ProfileRepository profileRepository, FollowersRepository followersRepository, JwtService jwtService){
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.followersRepository = followersRepository;
        this.jwtService = jwtService;
    }

    public FollowingResponseDTO findFollowing(UUID id) {
        Following following =  repository.findByProfileId(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found."));
        return toDto(following);
    }

    public void deleteFollowingByProfileId(UUID profile, UUID following, JwtAuthenticationToken token){
        Profile theProfile = profileRepository.findById(profile).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Profile theFollowing = profileRepository.findById(following).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        boolean isValidated = jwtService.validateToken(theProfile.getUser(), token);

        if (!isValidated){
            throw new UserUnauthorizedException();
        }


        if (!isFollowing(theFollowing, theProfile.getFollowingEntity().getFollowing())){
            throw new FollowingException("The user does not have this following.");
        }

        theProfile.getFollowingEntity().getFollowing().remove(theFollowing);
        repository.save(theProfile.getFollowingEntity());

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

    public boolean isFollowing(Profile following, List<Profile> profileList){
        return profileList.contains(following);
    }

}
