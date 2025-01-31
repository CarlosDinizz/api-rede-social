package com.api.redeSocialApi.domain;

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
@Table(name = "following")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Following {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(
            name = "following_users",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> following = new ArrayList<>();

    public Following(User following){
        this.user = following ;
    }
}

