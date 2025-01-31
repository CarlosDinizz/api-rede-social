package com.api.redeSocialApi.repositories;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowersRepository extends JpaRepository<Followers, UUID> {
    Followers findByUserId(UUID id);
    List<User> findFollowersByUserId(UUID id);
}
