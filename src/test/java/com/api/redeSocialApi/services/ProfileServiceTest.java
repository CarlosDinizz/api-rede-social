package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.*;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.ProfileUsernameException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.ProfileRequestDTO;
import com.api.redeSocialApi.dtos.ProfileResponseDTO;
import com.api.redeSocialApi.repositories.FollowersRepository;
import com.api.redeSocialApi.repositories.FollowingRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private ProfileRepository repository;

    @Mock
    private FollowingRepository followingRepository;

    @Mock
    private FollowersRepository followersRepository;

    @Mock
    private JwtService jwtService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Followers> followersArgumentCaptor;

    @Captor
    ArgumentCaptor<Following> followingArgumentCaptor;

    @Captor
    ArgumentCaptor<ProfileRequestDTO> requestDTOArgumentCaptor;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    ArgumentCaptor<JwtAuthenticationToken> tokenArgumentCaptor;

    @InjectMocks
    @Autowired
    private ProfileService service;

    static AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Nested
    class findProfile {

        @Test
        void shouldFindProfileWithSuccess() {
            UUID id = UUID.randomUUID();
            Optional<Profile> profile = Optional.of(new Profile(id,
                    "username",
                    "",
                    List.of(new Post()),
                    new Followers(),
                    new Following(),
                    new User()));
            doReturn(profile).when(repository).findById(uuidArgumentCaptor.capture());

            ProfileResponseDTO response = service.findProfile(id);
            assertNotNull(response);
            assertEquals(id, uuidArgumentCaptor.getValue());
        }

        @Test
        void shouldFindProfileWithError() {
            UUID id = UUID.randomUUID();
            ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> service.findProfile(id));
            assertEquals("Profile not found", exception.getMessage());
        }
    }

    @Nested
    class registerProfile {

        @Test
        void shouldRegisterProfile() {
            Profile profile = new Profile();
            profile.setUsername("username");
            profile.setBio("");
            profile.setPosts(List.of(new Post()));
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());
            profile.setUser(new User());

            UUID id = UUID.randomUUID();

            Profile savedProfile = new Profile(
                    id,
                    profile.getUsername(),
                    profile.getBio(),
                    profile.getPosts(),
                    profile.getFollowerEntity(),
                    profile.getFollowingEntity(),
                    profile.getUser());


            doReturn(savedProfile).when(repository).save(profile);

            service.registerProfile(profile);

            verify(followersRepository, times(1)).save(followersArgumentCaptor.capture());
            verify(followingRepository, times(1)).save(followingArgumentCaptor.capture());
        }
    }

    @Nested
    class UpdateProfile {

        @Test
        void shouldUpdateProfileWithBioSuccess() {
            UUID id = UUID.randomUUID();
            ProfileRequestDTO requestDTO = new ProfileRequestDTO("username", "");
            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setPosts(List.of(new Post()));
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());
            profile.setUser(new User());

            doReturn(false).when(repository).existsByUsername(requestDTO.username());
            doReturn(Optional.of(profile)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            service.updateProfile(id, requestDTO, token);

            assertNotNull(requestDTO.bio());
            assertEquals(requestDTO.username(), profile.getUsername());
            assertEquals(requestDTO.bio(), profile.getBio());


            verify(repository, times(1)).save(profile);
        }

        @Test
        void shouldUpdateProfileWithoutBioSuccess() {
            UUID id = UUID.randomUUID();
            ProfileRequestDTO requestDTO = new ProfileRequestDTO("username", null);
            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setPosts(List.of(new Post()));
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());
            profile.setUser(new User());

            doReturn(false).when(repository).existsByUsername(requestDTO.username());
            doReturn(Optional.of(profile)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            service.updateProfile(id, requestDTO, token);

            assertEquals(requestDTO.username(), profile.getUsername());

            verify(repository, times(1)).save(profile);
            verify(repository, times(1)).existsByUsername(requestDTO.username());
            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());
        }

        @Test
        void shouldNotUpdateProfileWhenUsernameExists() {

            UUID id = UUID.randomUUID();
            ProfileRequestDTO requestDTO = new ProfileRequestDTO("username", null);
            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setPosts(List.of(new Post()));
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());
            profile.setUser(new User());

            doReturn(true).when(repository).existsByUsername(requestDTO.username());

            ProfileUsernameException exception = assertThrows(ProfileUsernameException.class, () -> service.updateProfile(id, requestDTO, token));

            verify(repository, times(1)).existsByUsername(requestDTO.username());
            verify(repository, times(0)).findById(any());
            verify(jwtService, times(0)).validateToken(any(), any());

            assertEquals("Username already exists", exception.getMessage());
        }

        @Test
        void shouldNotUpdateProfileWhenTokenIsInvalid() {

            UUID id = UUID.randomUUID();
            ProfileRequestDTO requestDTO = new ProfileRequestDTO("username", null);
            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setPosts(List.of(new Post()));
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());
            profile.setUser(new User());

            doReturn(false).when(repository).existsByUsername(requestDTO.username());
            doReturn(Optional.of(profile)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(false).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> service.updateProfile(id, requestDTO, token));

            verify(repository, times(1)).existsByUsername(requestDTO.username());
            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());

            assertEquals("User does not have permission", exception.getMessage());

        }
    }
}