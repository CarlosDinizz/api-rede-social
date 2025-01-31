package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.services.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Following getFollowing(@PathVariable UUID id){
        return service.findFollowing(id);
    }

}
