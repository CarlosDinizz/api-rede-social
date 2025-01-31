package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Boolean is_activated;
    private Integer followers;
    private Integer following;

    public UserDTO(User user){
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        is_activated = user.getIsActivated();
        followers = 0;
        following = 0;
    }
}
