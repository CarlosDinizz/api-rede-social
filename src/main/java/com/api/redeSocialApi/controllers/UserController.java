package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.UserDTO;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.repositories.UserRepository;
import com.api.redeSocialApi.ResponseId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository repository;

    @Autowired
    public UserController(UserRepository repository){
        this.repository = repository;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUser(@PathVariable UUID id){
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isPresent()){
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(userDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ResponseId> registerUser(@RequestBody UserDTO userDTO){
        User user = new User(userDTO);
        user = repository.save(user);
        return ResponseEntity.ok(new ResponseId(user.getId()));
    }
}
