package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
