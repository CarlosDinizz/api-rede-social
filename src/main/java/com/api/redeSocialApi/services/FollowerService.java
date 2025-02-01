package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowerService {

    private FollowersRepository repository;
    private UserRepository userRepository;
    private FollowingRepository followingRepository;

    @Autowired
    public FollowerService(FollowersRepository repository, UserRepository userRepository, FollowingRepository followingRepository){
        this.repository = repository;
        this.userRepository = userRepository;
        this.followingRepository = followingRepository;
    }

    public void addFollower(UUID userId, UUID followerId){
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("User not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Followers followers = repository.findByUserId(userId);


        Following following = followingRepository.findByUserId(followerId);

        if (following.getFollowing().contains(user)){
            throw new RuntimeException("The user already follows");
        }

        following.getFollowing().add(user);
        followingRepository.save(following);

        followers.getFollowers().add(follower);
        repository.save(followers);
        userRepository.save(follower);

    }

    public Followers findFollower(UUID id) {
        return repository.findByUserId(id);
    }
    
}
