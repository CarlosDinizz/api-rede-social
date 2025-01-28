package com.api.redeSocialApi.dtos;

import com.api.redeSocialApi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FollowerDTO {

    private UUID id;
    private Integer quantity;
    private User user;
    private List<User> followers;


}
