package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.Role;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.domain.exceptions.UserEmailException;
import com.api.redeSocialApi.dtos.RegisterLoginRequestDTO;
import com.api.redeSocialApi.dtos.RegisterLoginResponseDTO;
import com.api.redeSocialApi.dtos.RegisterRequestDTO;
import com.api.redeSocialApi.dtos.UserResponseCreatedDTO;
import com.api.redeSocialApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegisterService {

    private UserRepository repository;
    private ProfileService profileService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;

    @Autowired
    public RegisterService(UserRepository repository, ProfileService profileService, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.repository = repository;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public void registerUser(RegisterRequestDTO request){

        if (userExists(request.email())){
            throw new UserEmailException("Email already in use");
        }

        User user = createUser(request);
        user = repository.save(user);
        profileService.registerProfile(new Profile(request.username(), user));
    }

    private Boolean userExists(String email){
        Optional<User> user = repository.findByEmail(email);
        return user.isPresent();
    }


    public RegisterLoginResponseDTO login(RegisterLoginRequestDTO request) {
        Optional<User> user = repository.findByEmail(request.email());

        if (user.isEmpty() || !isValidPassword(request.password(), user.get().getPassword())){
            throw new BadCredentialsException("Invalid email or password");
        }

        var jwtValue = generateToken(user.get());

        return new RegisterLoginResponseDTO(jwtValue, user.get().getProfile().getId());
    }

    private User createUser(RegisterRequestDTO request){
        User user = new User();
        user.setFirstName(request.first_name());
        user.setLastName(request.last_name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setIsEnabled(true);
        user.setRole(Role.ADMIN);
        return user;
    }

    private Boolean isValidPassword(String passwordRequest, String thePassword){
        return passwordEncoder.matches(passwordRequest, thePassword);
    }

    private String generateToken(User user){
        var scope = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(3600);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("api-rede-social")
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
