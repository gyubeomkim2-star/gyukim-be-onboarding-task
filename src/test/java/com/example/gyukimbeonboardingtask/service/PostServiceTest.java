package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.repository.mongodb.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private PostService postService;

    private Post samplePost;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository);

        samplePost = new Post();
        samplePost.setId("1");
        samplePost.setAuthorId("author1");
        samplePost.setContent("Test content");
    }

    @Test
    public void testCreatePostShouldReturnSavedPost() {
        when(postRepository.save(any(Post.class))).thenReturn(samplePost);

        Post result = postService.createPost(samplePost);

        assertNotNull(result);
        assertEquals(samplePost.getId(), result.getId());
        verify(postRepository).save(samplePost);
    }

    @Test
    public void testGetPostByIdWhenPostExistsShouldReturnPost() {
        when(postRepository.findById("1")).thenReturn(Optional.of(samplePost));

        Optional<Post> result = postService.getPostById("1");

        assertTrue(result.isPresent());
        assertEquals(samplePost.getId(), result.get().getId());
    }

    @Test
    public void testGetPostByIdWhenPostDoesNotExistShouldReturnEmpty() {
        when(postRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostById("999");

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllPostsShouldReturnAllPosts() {
        List<Post> posts = List.of(samplePost);
        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void GetPostsByAuthorIdShouldReturnAuthorPosts() {
        List<Post> authorPosts = List.of(samplePost);
        when(postRepository.findByAuthorId("author1")).thenReturn(authorPosts);

        List<Post> result = postService.getPostsByAuthorId("author1");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("author1", result.get(0).getAuthorId());
    }

    @Test
    public void testDeletePostWhenPostExistsShouldReturnTrue() {
        when(postRepository.existsById("1")).thenReturn(true);
        doNothing().when(postRepository).deleteById("1");

        boolean result = postService.deletePost("1");

        assertTrue(result);
        verify(postRepository).deleteById("1");
    }

    @Test
    public void testDeletePostWhenPostDoesNotExistShouldReturnFalse() {
        when(postRepository.existsById("999")).thenReturn(false);

        boolean result = postService.deletePost("999");

        assertFalse(result);
        verify(postRepository, never()).deleteById(any());
    }
}
