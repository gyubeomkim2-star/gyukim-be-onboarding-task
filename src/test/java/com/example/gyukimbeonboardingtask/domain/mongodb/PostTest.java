package com.example.gyukimbeonboardingtask.domain.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PostTest {
    private Post post;
    private String id;
    private String authorId;
    private String title;
    private String content;
    private String imageUrl;
    private List<String> tags;
    private List<String> likes;

    @BeforeEach
    public void setUp() {
        post = new Post();
        id = "123";
        authorId = "author1";
        title = "Test Title";
        content = "Test Content";
        imageUrl = "http://example.com/image.jpg";
        tags = Arrays.asList("tag1", "tag2");
        likes = Arrays.asList("user1", "user2");

        post.setId(id);
        post.setAuthorId(authorId);
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setTags(tags);
        post.setLikes(likes);
    }

    @Test
    public void testPostProperties() {
        assertEquals(id, post.getId());
        assertEquals(authorId, post.getAuthorId());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
        assertEquals(imageUrl, post.getImageUrl());
        assertEquals(tags, post.getTags());
        assertEquals(likes, post.getLikes());
    }

    @Test
    public void testEqualsAndHashCode() {
        Post post1 = new Post();
        Post post2 = new Post();

        post1.setId("123");
        post1.setTitle("Test");
        post1.setAuthorId("author1");
        post1.setContent("content");
        post1.setImageUrl("image.jpg");
        post1.setTags(List.of("tag1"));
        post1.setLikes(List.of("user1"));

        post2.setId("123");
        post2.setTitle("Test");
        post2.setAuthorId("author1");
        post2.setContent("content");
        post2.setImageUrl("image.jpg");
        post2.setTags(List.of("tag1"));
        post2.setLikes(List.of("user1"));

        assertEquals(post1, post2);
        assertEquals(post1.hashCode(), post2.hashCode());
    }

    @Test
    public void testToString() {
        String toString = post.toString();

        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("title=" + title));
        assertTrue(toString.contains("authorId=" + authorId));
        assertTrue(toString.contains("content=" + content));
        assertTrue(toString.contains("imageUrl=" + imageUrl));
        assertTrue(toString.contains("tags=" + tags));
        assertTrue(toString.contains("likes=" + likes));
    }

    @Test
    public void testCanEqual() {
        Post post1 = new Post();
        Post post2 = new Post();
        assertTrue(post1.canEqual(post2));
        assertNotEquals(true, post1.canEqual(new Object()));
    }
}
