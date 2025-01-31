package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.repositories.FollowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FollowingService {

    private FollowingRepository repository;

    @Autowired
    public FollowingService(FollowingRepository repository){
        this.repository = repository;
    }

    public Following findFollowing(UUID id) {
        return repository.findByUserId(id);
    }
}
