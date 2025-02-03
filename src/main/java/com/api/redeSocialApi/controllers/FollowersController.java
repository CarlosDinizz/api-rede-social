package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.dtos.FollowerResponseCreatedDTO;
import com.api.redeSocialApi.dtos.FollowerResponseDTO;
import com.api.redeSocialApi.services.FollowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FollowerResponseCreatedDTO> addFollower(@RequestParam("profileId")UUID profileId, @RequestParam("followerId") UUID followerId){
        FollowerResponseCreatedDTO responseDTO = followerService.addFollower(profileId, followerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FollowerResponseDTO> getFollowers(@PathVariable UUID id){
        FollowerResponseDTO follower = followerService.findFollower(id);
        return ResponseEntity.ok(follower);
    }
}
