package com.example.gyukimbeonboardingtask.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedResponseTest {

    @Test
    public void testAllArgsConstructorAndGetters() {
        // given
        List<String> postIds = Arrays.asList("post1", "post2");
        Long nextCursor = 12345L;
        boolean hasMore = true;

        // when
        FeedResponse feedResponse = new FeedResponse(postIds, nextCursor, hasMore);

        // then
        assertEquals(postIds, feedResponse.getPostIds());
        assertEquals(nextCursor, feedResponse.getNextCursor());
        assertTrue(feedResponse.isHasMore());
    }

    @Test
    public void testNoArgsConstructorAndSetters() {
        // given
        FeedResponse feedResponse = new FeedResponse();
        List<String> postIds = Arrays.asList("post3", "post4");
        Long nextCursor = 67890L;
        boolean hasMore = false;

        // when
        feedResponse.setPostIds(postIds);
        feedResponse.setNextCursor(nextCursor);
        feedResponse.setHasMore(hasMore);

        // then
        assertEquals(postIds, feedResponse.getPostIds());
        assertEquals(nextCursor, feedResponse.getNextCursor());
        assertFalse(feedResponse.isHasMore());
    }

    @Test
    public void testEqualsAndHashCode() {
        // given
        List<String> postIds1 = Arrays.asList("post1", "post2");
        Long nextCursor1 = 12345L;
        boolean hasMore1 = true;
        FeedResponse feedResponse1 = new FeedResponse(postIds1, nextCursor1, hasMore1);

        List<String> postIds2 = Arrays.asList("post1", "post2");
        Long nextCursor2 = 12345L;
        boolean hasMore2 = true;
        FeedResponse feedResponse2 = new FeedResponse(postIds2, nextCursor2, hasMore2);

        List<String> postIds3 = Arrays.asList("post3", "post4");
        Long nextCursor3 = 67890L;
        boolean hasMore3 = false;
        FeedResponse feedResponse3 = new FeedResponse(postIds3, nextCursor3, hasMore3);

        // then
        assertEquals(feedResponse1, feedResponse2);
        assertNotEquals(feedResponse1, feedResponse3);
        assertEquals(feedResponse1.hashCode(), feedResponse2.hashCode());
        assertNotEquals(feedResponse1.hashCode(), feedResponse3.hashCode());
    }

    @Test
    public void testToString() {
        // given
        List<String> postIds = Arrays.asList("post1", "post2");
        Long nextCursor = 12345L;
        boolean hasMore = true;
        FeedResponse feedResponse = new FeedResponse(postIds, nextCursor, hasMore);

        // when
        String feedResponseString = feedResponse.toString();

        // then
        assertTrue(feedResponseString.contains("postIds=" + postIds));
        assertTrue(feedResponseString.contains("nextCursor=" + nextCursor));
        assertTrue(feedResponseString.contains("hasMore=" + hasMore));
    }
}
