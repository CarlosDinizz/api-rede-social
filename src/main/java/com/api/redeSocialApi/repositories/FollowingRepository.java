package com.api.redeSocialApi.repositories;

import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowingRepository extends JpaRepository<Following, UUID> {
    Optional<Following> findByProfileId(UUID id);
}
