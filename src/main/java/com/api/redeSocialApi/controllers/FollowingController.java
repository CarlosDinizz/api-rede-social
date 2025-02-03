package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.dtos.FollowingResponseDTO;
import com.api.redeSocialApi.services.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/following")
public class FollowingController {

    private FollowingService service;

    @Autowired
    public FollowingController(FollowingService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FollowingResponseDTO> getFollowing(@PathVariable UUID id){
        FollowingResponseDTO responseDTO = service.findFollowing(id);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFollowingByUserId(@RequestParam("profileId") UUID profileId, @RequestParam("followingId") UUID followingId){
        service.deleteFollowingByProfileId(profileId, followingId);
        return ResponseEntity.noContent().build();
    }
}
