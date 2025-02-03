package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.UserRequestCreatedDTO;
import com.api.redeSocialApi.dtos.UserResponseCreatedDTO;
import com.api.redeSocialApi.dtos.UserResponseDTO;
import com.api.redeSocialApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUser(@PathVariable UUID id){
        UserResponseDTO response = userService.findUser(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseCreatedDTO> registerUser(@RequestBody UserRequestCreatedDTO request){
        UserResponseCreatedDTO response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
