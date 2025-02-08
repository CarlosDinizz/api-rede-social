package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.ProfileRequestDTO;
import com.api.redeSocialApi.dtos.ProfileResponseDTO;
import com.api.redeSocialApi.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    public ProfileService service;

    @Autowired
    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> findProfile(@PathVariable UUID id){
        ProfileResponseDTO response = service.findProfile(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable UUID id, @RequestBody ProfileRequestDTO request, JwtAuthenticationToken token){
        service.updateProfile(id, request, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
