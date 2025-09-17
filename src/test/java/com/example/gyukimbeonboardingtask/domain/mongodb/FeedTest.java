package com.example.gyukimbeonboardingtask.domain.mongodb;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FeedTest {

    @Test
    public void testGettersAndSetters() {
        // given
        Feed feed = new Feed();
        String id = "feedId123";
        Long userId = 1L;
        String postId = "postId456";
        Long createdAt = System.currentTimeMillis();

        // when
        feed.setId(id);
        feed.setUserId(userId);
        feed.setPostId(postId);
        feed.setCreatedAt(createdAt);

        // then
        assertEquals(id, feed.getId());
        assertEquals(userId, feed.getUserId());
        assertEquals(postId, feed.getPostId());
        assertEquals(createdAt, feed.getCreatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        // given
        Long createdAt = System.currentTimeMillis();

        Feed feed1 = new Feed();
        feed1.setId("feedId123");
        feed1.setUserId(1L);
        feed1.setPostId("postId456");
        feed1.setCreatedAt(createdAt);

        Feed feed2 = new Feed();
        feed2.setId("feedId123");
        feed2.setUserId(1L);
        feed2.setPostId("postId456");
        feed2.setCreatedAt(createdAt);

        Feed feed3 = new Feed();
        feed3.setId("feedId789");
        feed3.setUserId(2L);
        feed3.setPostId("postId987");
        feed3.setCreatedAt(createdAt + 1000);


        // then
        assertEquals(feed1, feed2);
        assertNotEquals(feed1, feed3);
        assertEquals(feed1.hashCode(), feed2.hashCode());
        assertNotEquals(feed1.hashCode(), feed3.hashCode());
    }

    @Test
    public void testToString() {
        // given
        Feed feed = new Feed();
        String id = "feedId123";
        Long userId = 1L;
        String postId = "postId456";
        Long createdAt = System.currentTimeMillis();
        feed.setId(id);
        feed.setUserId(userId);
        feed.setPostId(postId);
        feed.setCreatedAt(createdAt);

        // when
        String feedString = feed.toString();

        // then
        assertTrue(feedString.contains("id=" + id));
        assertTrue(feedString.contains("userId=" + userId));
        assertTrue(feedString.contains("postId=" + postId));
        assertTrue(feedString.contains("createdAt=" + createdAt));
    }
}
