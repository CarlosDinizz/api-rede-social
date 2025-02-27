package com.api.redeSocialApi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, targetEntity = Followers.class)
    @JsonIgnore
    private Followers followerEntity;

    @JsonIgnore
    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, targetEntity = Following.class)
    private Following followingEntity;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Profile(String username, User user) {
        this.username = username;
        this.user = user;
        bio = "";
    }
}
