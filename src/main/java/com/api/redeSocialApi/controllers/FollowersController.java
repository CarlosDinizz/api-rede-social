package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.services.FollowerService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/followers")
public class FollowersController {

    private FollowerService followerService;

    public FollowersController(FollowerService followerService){
        this.followerService = followerService;
    }

    @PostMapping
    public void addFollower(@RequestParam("userId")UUID userId, @RequestParam("followerId") UUID followerId){
        followerService.addFollower(userId, followerId);
    }

    @GetMapping("/{id}")
    public Followers getFollowers(@PathVariable UUID id){
        return followerService.findFollower(id);
    }

//    @DeleteMapping
//    public void deleteFollower(@RequestParam("userId")UUID userId, @RequestParam("followerId") UUID followerId){
//        followerService.removeFollower(userId, followerId);
//    }

}
