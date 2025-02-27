package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public Boolean validateToken(User user, JwtAuthenticationToken token){
        return user.getEmail().equals(token.getName());
    }

}
