package com.example.gyukimbeonboardingtask.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    private PostController postController;

    @BeforeEach
    public void setUp() {
        postController = new PostController(postService);
    }

    @Test
    public void testCreatePostShouldReturnCreatedPost() {
        Post post = new Post();
        when(postService.createPost(post)).thenReturn(post);

        ResponseEntity<Post> response = postController.createPost(post);

        verify(postService).createPost(post);
        assertNotNull(response.getBody());
        assertEquals(post, response.getBody());
    }

    @Test
    public void testGetPostsByAuthorIdShouldReturnPostsList() {
        String authorId = "1";
        List<Post> expectedPosts = Arrays.asList(new Post(), new Post());
        when(postService.getPostsByAuthorId(authorId)).thenReturn(expectedPosts);

        ResponseEntity<List<Post>> response = postController.getPostsByAuthorId(authorId);

        verify(postService).getPostsByAuthorId(authorId);
        assertNotNull(response.getBody());
        assertEquals(expectedPosts, response.getBody());
    }

    @Test
    public void testGetPostsByAuthorIdShouldReturnNotFoundWhenEmpty() {
        String authorId = "1";
        when(postService.getPostsByAuthorId(authorId)).thenReturn(List.of());

        ResponseEntity<List<Post>> response = postController.getPostsByAuthorId(authorId);

        verify(postService).getPostsByAuthorId(authorId);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testDeletePostShouldReturnNoContent() {
        String postId = "1";
        when(postService.deletePost(postId)).thenReturn(true);

        ResponseEntity<Void> response = postController.deletePost(postId);

        verify(postService).deletePost(postId);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    public void testDeletePostShouldReturnNotFoundWhenUnsuccessful() {
        String postId = "1";
        when(postService.deletePost(postId)).thenReturn(false);

        ResponseEntity<Void> response = postController.deletePost(postId);

        verify(postService).deletePost(postId);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void testGetAllPostsShouldReturnPostsList() {
        List<Post> expectedPosts = Arrays.asList(new Post(), new Post());
        when(postService.getAllPosts()).thenReturn(expectedPosts);

        ResponseEntity<List<Post>> response = postController.getAllPosts();

        verify(postService).getAllPosts();
        assertNotNull(response.getBody());
        assertEquals(expectedPosts, response.getBody());
    }

    @Test
    public void testAddLikeToPostShouldReturnOkWhenSuccessful() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.addLike(postId, "user1")).thenReturn(true);

        ResponseEntity<Void> response = postController.addLikeToPost(postId, payload);

        verify(postService).addLike(postId, "user1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddLikeToPostShouldReturnBadRequestWhenUserIdMissing() {
        String postId = "1";
        Map<String, String> payload = Map.of();

        ResponseEntity<Void> response = postController.addLikeToPost(postId, payload);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddLikeToPostShouldReturnNotFoundWhenPostNotExists() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.addLike(postId, "user1")).thenReturn(false);
        when(postService.getPostById(postId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Void> response = postController.addLikeToPost(postId, payload);

        verify(postService).addLike(postId, "user1");
        verify(postService).getPostById(postId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddLikeToPostShouldReturnConflictWhenAlreadyLiked() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.addLike(postId, "user1")).thenReturn(false);
        when(postService.getPostById(postId)).thenReturn(java.util.Optional.of(new Post()));

        ResponseEntity<Void> response = postController.addLikeToPost(postId, payload);

        verify(postService).addLike(postId, "user1");
        verify(postService).getPostById(postId);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testRemoveLikeFromPostShouldReturnNoContentWhenSuccessful() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.removeLike(postId, "user1")).thenReturn(true);

        ResponseEntity<Void> response = postController.removeLikeFromPost(postId, payload);

        verify(postService).removeLike(postId, "user1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testRemoveLikeFromPostShouldReturnBadRequestWhenUserIdMissing() {
        String postId = "1";
        Map<String, String> payload = Map.of();

        ResponseEntity<Void> response = postController.removeLikeFromPost(postId, payload);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRemoveLikeFromPostShouldReturnNotFoundWhenPostNotExists() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.removeLike(postId, "user1")).thenReturn(false);
        when(postService.getPostById(postId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Void> response = postController.removeLikeFromPost(postId, payload);

        verify(postService).removeLike(postId, "user1");
        verify(postService).getPostById(postId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveLikeFromPostShouldReturnNotModifiedWhenNotLiked() {
        String postId = "1";
        Map<String, String> payload = Map.of("userId", "user1");
        when(postService.removeLike(postId, "user1")).thenReturn(false);
        when(postService.getPostById(postId)).thenReturn(java.util.Optional.of(new Post()));

        ResponseEntity<Void> response = postController.removeLikeFromPost(postId, payload);

        verify(postService).removeLike(postId, "user1");
        verify(postService).getPostById(postId);
        assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
    }
}
