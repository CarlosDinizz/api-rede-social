package com.api.redeSocialApi.repositories;

import com.api.redeSocialApi.domain.Followers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowersRepository extends JpaRepository<Followers, UUID> {
    Followers findFollowersByUserAssociatedId(UUID id);
}
