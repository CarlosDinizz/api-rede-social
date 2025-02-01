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
public class FollowingService {

    private FollowingRepository repository;
    private UserRepository userRepository;
    private FollowersRepository followersRepository;

    @Autowired
    public FollowingService(FollowingRepository repository, UserRepository userRepository, FollowersRepository followersRepository){
        this.repository = repository;
        this.userRepository = userRepository;
        this.followersRepository = followersRepository;
    }

    public Following findFollowing(UUID id) {
        return repository.findByUserId(id);
    }

    public void deleteFollowingByUserId(UUID user, UUID following){
        User theUser = userRepository.findById(user).orElseThrow();
        User theFollowing = userRepository.findById(following).orElseThrow();

        Following followingEntity = theUser.getFollowingEntity();
        if (!followingEntity.getFollowing().contains(theFollowing)){
            throw new RuntimeException();
        }
        followingEntity.getFollowing().remove(theFollowing);
        repository.save(followingEntity);

        Followers theFollowerFromTheFollowing = theFollowing.getFollowerEntity();
        theFollowerFromTheFollowing.getFollowers().remove(theUser);
        followersRepository.save(theFollowerFromTheFollowing);
    }

}
