package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.Profile;
import com.api.redeSocialApi.domain.Role;
import com.api.redeSocialApi.domain.User;
import com.api.redeSocialApi.domain.exceptions.UserNotFoundException;
import com.api.redeSocialApi.dtos.UserResponseDTO;
import com.api.redeSocialApi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Nested
    class findUser{

        @Test
        void shouldFindUserWithSuccess(){

            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUsername("username");

            UUID uuid = UUID.randomUUID();
            User user = new User();
            user.setId(uuid);
            user.setEmail("email@gmail.com");
            user.setPassword("password123");
            user.setIsEnabled(true);
            user.setFirstName("fname");
            user.setLastName("lname");
            user.setProfile(profile);

            doReturn(Optional.of(user)).when(repository).findById(uuidArgumentCaptor.capture());

            UserResponseDTO testResponseDTO = new UserResponseDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfile().getId());
            UserResponseDTO responseDTO = service.findUser(uuid);

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());

            assertEquals(uuid, uuidArgumentCaptor.getValue());
            assertEquals(testResponseDTO, responseDTO);
            assertNotNull(uuid);
        }

        @Test
        void shouldNotFindUserWhenUserNotFound() {
            UUID id = UUID.randomUUID();

            doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> service.findUser(id));

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());

            assertEquals(id, uuidArgumentCaptor.getValue());
        }
    }
}