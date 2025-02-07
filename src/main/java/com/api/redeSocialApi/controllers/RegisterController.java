package com.api.redeSocialApi.controllers;

import com.api.redeSocialApi.dtos.RegisterLoginRequestDTO;
import com.api.redeSocialApi.dtos.RegisterLoginResponseDTO;
import com.api.redeSocialApi.dtos.RegisterRequestDTO;
import com.api.redeSocialApi.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private RegisterService service;

    @Autowired

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestDTO request){
        service.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<RegisterLoginResponseDTO> login(@RequestBody RegisterLoginRequestDTO request){
        RegisterLoginResponseDTO response = service.login(request);
        return ResponseEntity.ok(response);
    }
}
