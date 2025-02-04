package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.domain.exceptions.FollowingExistsException;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.dtos.FollowerResponseCreatedDTO;
import com.api.redeSocialApi.dtos.FollowerResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowerService {

    private FollowersRepository repository;
    private ProfileRepository profileRepository;
    private FollowingRepository followingRepository;

    @Autowired
    public FollowerService(FollowersRepository repository, ProfileRepository profileRepository, FollowingRepository followingRepository){
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.followingRepository = followingRepository;
    }

    public FollowerResponseCreatedDTO addFollower(UUID userId, UUID followerId){
        Profile follower = profileRepository.findById(followerId).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Profile profile = profileRepository.findById(userId).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        Followers followers = repository.findByProfileId(userId);


        Following following = followingRepository.findByProfileId(followerId);

        if (following.getFollowing().contains(profile)){
            throw new FollowingExistsException("The user already follows");
        }

        following.getFollowing().add(profile);
        followingRepository.save(following);

        followers.getFollowers().add(follower);
        followers = repository.save(followers);
        return toResponseCreatedDto(followers, follower);
    }

    public FollowerResponseDTO findFollower(UUID id) {
        Followers followers = repository.findByProfileId(id);
        return toResponseDto(followers);
    }

    public FollowerResponseCreatedDTO toResponseCreatedDto(Followers user, Profile follower){
        return new FollowerResponseCreatedDTO(user.getId(), user.getProfile().getUsername(), follower.getUsername());
    }

    public FollowerResponseDTO toResponseDto(Followers followers){
        return new FollowerResponseDTO(
                followers.getId(),
                followers.getProfile().getId(),
                followers.getProfile().getUsername(),
                followers.getFollowers()
        );
    }
}
