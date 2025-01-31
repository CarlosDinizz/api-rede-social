package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.dtos.ResponseId;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.dtos.UserDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository repository;
    private FollowingRepository followingRepository;
    private FollowersRepository followersRepository;

    @Autowired
    public UserService(UserRepository repository, FollowingRepository followingRepository, FollowersRepository followersRepository){
        this.repository = repository;
        this.followingRepository = followingRepository;
        this.followersRepository = followersRepository;
    }

    public UserDTO findUser(UUID id){
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }

    public ResponseId registerUser(UserDTO userDTO){
        User user = new User(userDTO);
        user = repository.save(user);
        followersRepository.save(new Followers(user));
        followingRepository.save(new Following(user));
        return new ResponseId(user.getId());
    }
}
