package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.UserDTO;
import com.api.redeSocialApi.repositories.ResponseId;
import com.api.redeSocialApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<UserDTO> findUser(@PathVariable UUID id){
        UserDTO userDTO = userService.findUser(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<ResponseId> registerUser(@RequestBody UserDTO userDTO){
        ResponseId id = userService.registerUser(userDTO);
        return ResponseEntity.ok(id);
    }
}
