package com.api.redeSocialApi.domain;

import com.api.redeSocialApi.dtos.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_activated", nullable = false)
    private Boolean isActivated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, targetEntity = Followers.class)
    @JsonIgnore
    private Followers followerEntity;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, targetEntity = Following.class)
    private Following followingEntity;

    @Column(name = "followers")
    private Integer followers;

    @Column(name = "following")
    private Integer following;

    public User(UserDTO userDTO){
        this.name = userDTO.getName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        isActivated = true;
        followers = 0;
        following = 0;
    }

}
