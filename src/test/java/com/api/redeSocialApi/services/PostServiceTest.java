package com.api.redeSocialApi.services;

import com.api.redeSocialApi.domain.*;
import com.api.redeSocialApi.domain.exceptions.PostNotFoundException;
import com.api.redeSocialApi.domain.exceptions.ProfileNotFoundException;
import com.api.redeSocialApi.domain.exceptions.UserUnauthorizedException;
import com.api.redeSocialApi.dtos.PostRequestDTO;
import com.api.redeSocialApi.dtos.PostResponseCreatedDTO;
import com.api.redeSocialApi.dtos.PostResponseDTO;
import com.api.redeSocialApi.repositories.PostRepository;
import com.api.redeSocialApi.repositories.ProfileRepository;
import org.hibernate.annotations.Comments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository repository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PostService service;

    @Captor
    private ArgumentCaptor<Profile> profileArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<JwtAuthenticationToken> tokenArgumentCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class newPost{

        @Test
        void shouldCreateNewPostWithSuccess() {

            UUID id = UUID.randomUUID();
            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setUser(new User());
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(true).when(profileRepository).existsById(uuidArgumentCaptor.capture());
            doReturn(Optional.of(profile)).when(profileRepository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            Post post = service.toPost(requestDTO, profile);
            post.setId(UUID.randomUUID());
            doReturn(post).when(repository).save(postArgumentCaptor.capture());

            PostResponseCreatedDTO responseDTO = service.toPostCreatedDto(post);

            PostResponseCreatedDTO output = service.newPost(requestDTO, id, token);

            assertNotNull(responseDTO);
            assertNotNull(token);
            assertNotNull(profile);
            assertEquals(output, responseDTO);

            assertEquals(profile.getUser(), userArgumentCaptor.getValue());
            assertEquals(token, tokenArgumentCaptor.getValue());
        }

        @Test
        void shouldNotCreateNewPostWhenTokenIsInvalid() {

            UUID id = UUID.randomUUID();
            Profile profile = new Profile();
            profile.setId(id);
            profile.setUsername("username");
            profile.setBio("");
            profile.setUser(new User());
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
            boolean isValid = true;

            doReturn(true).when(profileRepository).existsById(uuidArgumentCaptor.capture());
            doReturn(Optional.of(profile)).when(profileRepository).findById(uuidArgumentCaptor.capture());
            doReturn(!isValid).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());


            UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> service.newPost(requestDTO, id, token));

            assertNotNull(token);
            assertNotNull(profile);

            assertEquals(id, uuidArgumentCaptor.getValue());
            assertEquals(profile.getUser(), userArgumentCaptor.getValue());
            assertEquals("User does not have permission", exception.getMessage());
        }

        @Test
        void shouldNotCreateNewPostWhenProfileIsEmpty() {

            UUID id = UUID.randomUUID();
            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUsername("username");
            profile.setBio("");
            profile.setUser(new User());
            profile.setFollowerEntity(new Followers());
            profile.setFollowingEntity(new Following());

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(false).when(profileRepository).existsById(uuidArgumentCaptor.capture());

            ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> service.newPost(requestDTO, id, token));

            assertNotNull(token);
            assertNotNull(id);

            assertNotSame(profile.getId(), uuidArgumentCaptor.getValue());
            assertEquals("Profile not found", exception.getMessage());
        }
    }

    @Nested
    class findPost{

        @Test
        void shouldFindPostWithSuccess() {
            Profile profile = new Profile();
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setId(UUID.randomUUID());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();
            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            doReturn(true).when(repository).existsById(uuidArgumentCaptor.capture());
            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());

            PostResponseDTO responseDTO = service.toDto(post);

            PostResponseDTO postResponseDTO = service.findPost(uuid);

            assertNotNull(uuid);
            assertNotNull(responseDTO);
            assertEquals(uuid, uuidArgumentCaptor.getAllValues().getFirst());
            assertEquals(post.getId(), uuidArgumentCaptor.getAllValues().get(1));
            assertEquals(responseDTO, postResponseDTO);
        }

        @Test
        void shouldNotFindPostWhenIdNotExists() {
            Profile profile = new Profile();
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setId(UUID.randomUUID());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();
            Post post = new Post();
            post.setId(UUID.randomUUID());
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            doReturn(false).when(repository).existsById(uuidArgumentCaptor.capture());

            PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> service.findPost(uuid));

            assertNotNull(uuid);
            assertEquals(uuid, uuidArgumentCaptor.getValue());
            assertEquals("Post not found", exception.getMessage());

            verify(repository, times(0)).findById(uuidArgumentCaptor.capture());
        }
    }

    @Nested
    class deletePost{

        @Test
        void shouldDeletePostWithSuccess() {

            Profile profile = new Profile();
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setId(UUID.randomUUID());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();
            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());
            doNothing().when(repository).deleteById(uuidArgumentCaptor.capture());

            service.deletePost(uuid, token);

            assertEquals(post.getId(), uuidArgumentCaptor.getAllValues().getFirst());
            assertEquals(post.getId(), uuidArgumentCaptor.getAllValues().get(1));
            assertEquals(post.getProfile().getUser(), userArgumentCaptor.getValue());
            assertEquals(token, tokenArgumentCaptor.getValue());

            verify(repository, times(1)).findById(uuidArgumentCaptor.getAllValues().getFirst());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());
            verify(repository, times(1)).deleteById(uuidArgumentCaptor.getAllValues().get(1));
        }

        @Test
        void shouldNotDeletePostWhenPostIdNotExists() {

            Profile profile = new Profile();
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setId(UUID.randomUUID());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();
            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());
            PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> service.deletePost(uuid, token));

            assertEquals(uuid, uuidArgumentCaptor.getValue());
            assertEquals("Post not found", exception.getMessage());

            verify(repository, times(0)).deleteById(any());
            verify(jwtService, times(0)).validateToken(any(), any());
        }

        @Test
        void shouldNotDeletePostWhenTokenIsInvalid() {

            Profile profile = new Profile();
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setId(UUID.randomUUID());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();
            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(false).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> service.deletePost(uuid, token));

            verify(repository, times(0)).deleteById(any());

            assertEquals(uuid, uuidArgumentCaptor.getAllValues().getFirst());
            assertEquals(token, tokenArgumentCaptor.getValue());
            assertEquals(post.getProfile().getUser(), userArgumentCaptor.getValue());
        }
    }

    @Nested
    class postsByProfileId{

        @Test
        void shouldFindPostsWithSuccess() {

            UUID uuid = UUID.randomUUID();

            Profile profile = new Profile();
            profile.setId(uuid);
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            Post post = new Post();
            post.setId(UUID.randomUUID());
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);


            List<Post> posts = List.of(post);

            doReturn(true).when(profileRepository).existsById(uuidArgumentCaptor.capture());
            doReturn(posts).when(repository).findPostByProfileId(uuidArgumentCaptor.capture());

            List<PostResponseDTO> dtoTest = posts.stream().map(service::toDto).toList();

            List<PostResponseDTO> dtoResponse = service.postsByProfileId(uuid);

            verify(profileRepository, times(1)).existsById(uuidArgumentCaptor.getAllValues().getFirst());
            verify(repository, times(1)).findPostByProfileId(uuidArgumentCaptor.getAllValues().get(1));

            assertEquals(dtoResponse, dtoTest);
            assertEquals(posts.getFirst().getProfile().getId(), uuidArgumentCaptor.getAllValues().get(1));

            assertNotNull(uuidArgumentCaptor.getValue());
        }

        @Test
        void shouldNotFindPostsWhenProfileNotExists(){

            UUID uuid = UUID.randomUUID();

            Profile profile = new Profile();
            profile.setId(uuid);
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            doReturn(false).when(profileRepository).existsById(uuidArgumentCaptor.capture());

            ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> service.postsByProfileId(uuid));

            verify(profileRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(repository, times(0)).findPostByProfileId(any());

            assertEquals(uuid, uuidArgumentCaptor.getValue());
            assertEquals("Profile not found", exception.getMessage());
        }

        @Test
        void shouldNotFindPostsWhenPostsNotExists() {
            UUID uuid = UUID.randomUUID();

            Profile profile = new Profile();
            profile.setId(uuid);
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            Post post = new Post();
            post.setId(UUID.randomUUID());
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);


            List<Post> posts = List.of();

            doReturn(true).when(profileRepository).existsById(uuidArgumentCaptor.capture());
            doReturn(List.of()).when(repository).findPostByProfileId(uuidArgumentCaptor.capture());

            List<PostResponseDTO> dtoTest = posts.stream().map(service::toDto).toList();

            PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> service.postsByProfileId(uuid));

            verify(profileRepository, times(1)).existsById(uuidArgumentCaptor.getAllValues().getFirst());
            verify(repository, times(1)).findPostByProfileId(uuidArgumentCaptor.getAllValues().get(1));

            assertEquals("Post not found", exception.getMessage());
        }
    }

    @Nested
    class updatePost{

        @Test
        void shouldUpdatePostWithSuccess() {


            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();

            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            post.setDescription(requestDTO.description());
            post.setImg(requestDTO.img());

            doReturn(post).when(repository).save(postArgumentCaptor.capture());

            service.updatePost(uuid, requestDTO, token);

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());
            verify(repository, times(1)).save(postArgumentCaptor.getValue());

            assertNotNull(requestDTO);
            assertNotNull(requestDTO.description());
            assertNotNull(requestDTO.img());
            assertEquals(post.getId(), uuidArgumentCaptor.getValue());
            assertEquals(post.getProfile().getUser(), userArgumentCaptor.getValue());
            assertEquals(token, tokenArgumentCaptor.getValue());
            assertEquals(post, postArgumentCaptor.getValue());
        }

        @Test
        void shouldNotUpdatePostWhenTokenIsInvalid() {


            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();

            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(false).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            UserUnauthorizedException exception = assertThrows(UserUnauthorizedException.class, () -> service.updatePost(uuid, requestDTO, token));

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());
            verify(repository, times(0)).save(any());


            assertEquals("User does not have permission", exception.getMessage());
            assertEquals(post.getId(), uuidArgumentCaptor.getValue());
            assertEquals(post.getProfile().getUser(), userArgumentCaptor.getValue());
            assertEquals(token, tokenArgumentCaptor.getValue());
        }

        @Test
        void shouldUpdatePostButNoChanges() {

            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();

            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            PostRequestDTO requestDTO = new PostRequestDTO(null, null, false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.of(post)).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());
            

            doReturn(post).when(repository).save(postArgumentCaptor.capture());

            service.updatePost(uuid, requestDTO, token);

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(1)).validateToken(userArgumentCaptor.getValue(), tokenArgumentCaptor.getValue());
            verify(repository, times(1)).save(postArgumentCaptor.getValue());


            assertNotNull(requestDTO);
            assertNull(requestDTO.description());
            assertNull(requestDTO.img());
            assertEquals(post.getId(), uuidArgumentCaptor.getValue());
            assertEquals(post.getProfile().getUser(), userArgumentCaptor.getValue());
            assertEquals(token, tokenArgumentCaptor.getValue());
        }

        @Test
        void shouldNotUpdatePostWhenPostNotFound() {

            Profile profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setFollowingEntity(new Following());
            profile.setFollowerEntity(new Followers());
            profile.setUser(new User());
            profile.setBio("");

            UUID uuid = UUID.randomUUID();

            Post post = new Post();
            post.setId(uuid);
            post.setDescription("");
            post.setLikes(1);
            post.setTime(LocalDateTime.now());
            post.setIsCommentsBlocked(false);
            post.setImg("");
            post.setComments(List.of(new Comment()));
            post.setProfile(profile);

            PostRequestDTO requestDTO = new PostRequestDTO("", "", false);

            JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);

            doReturn(Optional.empty()).when(repository).findById(uuidArgumentCaptor.capture());
            doReturn(true).when(jwtService).validateToken(userArgumentCaptor.capture(), tokenArgumentCaptor.capture());

            doReturn(post).when(repository).save(postArgumentCaptor.capture());

            PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> service.updatePost(uuid, requestDTO, token));

            verify(repository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(jwtService, times(0)).validateToken(any(), any());
            verify(repository, times(0)).save(any());


            assertNotNull(requestDTO);
            assertNotNull(token);
            assertEquals("Post not found", exception.getMessage());
        }
    }
}