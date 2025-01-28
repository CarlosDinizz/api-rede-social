package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowerService {

    private FollowersRepository repository;
    private UserRepository userRepository;

    @Autowired
    public FollowerService(FollowersRepository repository, UserRepository userRepository){
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void addFollower(UUID userId, UUID followerId){
        Followers followers = repository.findFollowersByUserAssociatedId(userId);
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("User not found"));

        if (followers != null){
            followers.getFollowers().add(follower);
            followers.setQuantity(followers.getQuantity() + 1);
            repository.save(followers);
            return;
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Followers newFollower = new Followers(user, follower);
        repository.save(newFollower);
    }

    public void removeFollower(UUID userId, UUID followerId){
        Followers followers = repository.findFollowersByUserAssociatedId(userId);

        if (followers.getQuantity() == 0){
            throw new RuntimeException("No followers");
        }
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("User not found"));

        followers.getFollowers().remove(follower);
        followers.setQuantity(followers.getQuantity() - 1);
        repository.save(followers);


    }
}
