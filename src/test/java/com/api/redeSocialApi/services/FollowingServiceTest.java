package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Followers;
import com.api.redeSocialApi.domain.Following;
import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.dtos.FollowingResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowingServiceTest {

    @Mock
    private FollowingRepository repository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private FollowersRepository followersRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private FollowingService service;

    private AutoCloseable autoCloseable;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<Following> followingArgumentCaptor;

    @Captor
    private ArgumentCaptor<Followers> followersArgumentCaptor;

    @Captor
    private ArgumentCaptor<Profile> profileArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<JwtAuthenticationToken> tokenArgumentCaptor;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Nested
    class findFollowing{

        @Test
        void findFollowingWithSuccess() {
            UUID id = UUID.randomUUID();

            Profile profile = new Profile();
            profile.setUsername("");
            profile.setBio("");
            profile.setId(id);

            Following following = new Following();
            following.setId(UUID.randomUUID());
            following.setProfile(profile);
            following.setFollowing(List.of(new Profile()));

            doReturn(Optional.of(following)).when(repository).findByProfileId(uuidArgumentCaptor.capture());

            FollowingResponseDTO dtoTest = new FollowingResponseDTO(following.getId(), id, profile.getUsername(), following.getFollowing());
            FollowingResponseDTO response = service.findFollowing(id);

            verify(repository, times(1)).findByProfileId(uuidArgumentCaptor.getValue());

            assertEquals(dtoTest, response);
            assertEquals(id, uuidArgumentCaptor.getValue());
        }

        @Test
        void findFollowingWithErrors() {
            UUID id = UUID.randomUUID();

            doReturn(Optional.empty()).when(repository).findByProfileId(uuidArgumentCaptor.capture());

            ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> service.findFollowing(id));

            verify(repository, times(1)).findByProfileId(uuidArgumentCaptor.getValue());

            assertEquals(id, uuidArgumentCaptor.getValue());
            assertEquals("Profile not found.", exception.getMessage());
        }
    }

}