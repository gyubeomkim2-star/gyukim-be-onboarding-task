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
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

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
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testDeletePostShouldReturnNoContent() {
        String postId = "1";
        when(postService.deletePost(postId)).thenReturn(true);

        ResponseEntity<Void> response = postController.deletePost(postId);

        verify(postService).deletePost(postId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testDeletePostShouldReturnNotFoundWhenUnsuccessful() {
        String postId = "1";
        when(postService.deletePost(postId)).thenReturn(false);

        ResponseEntity<Void> response = postController.deletePost(postId);

        verify(postService).deletePost(postId);
        assertEquals(404, response.getStatusCodeValue());
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
}
