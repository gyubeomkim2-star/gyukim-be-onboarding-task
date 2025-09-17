package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Feed;
import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.domain.mysql.User;
import com.example.gyukimbeonboardingtask.dto.FeedResponse;
import com.example.gyukimbeonboardingtask.repository.mongodb.FeedRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ZSetOperations<String, Object> zSetOperations;

    @InjectMocks
    private FeedService feedService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    void handlePostCreation_shouldSaveFeedForEachFollower() {
        // given
        Post post = new Post();
        post.setId("postId1");
        post.setAuthorId("1");
        post.setTitle("Test Post");
        post.setCreatedAt(System.currentTimeMillis());

        User author = new User();
        author.setUsername("author");
        when(userRepository.getUserById(1L)).thenReturn(author);

        List<Long> followerIds = Arrays.asList(10L, 11L, 12L);
        when(followRepository.getFollowerIdsByFollowingId(1L)).thenReturn(followerIds);

        // when
        feedService.handlePostCreation(post);

        // then
        ArgumentCaptor<Feed> feedCaptor = ArgumentCaptor.forClass(Feed.class);
        verify(feedRepository, times(followerIds.size())).save(feedCaptor.capture());

        List<Feed> capturedFeeds = feedCaptor.getAllValues();
        assertEquals(followerIds.size(), capturedFeeds.size());

        for (int i = 0; i < followerIds.size(); i++) {
            assertEquals(followerIds.get(i), capturedFeeds.get(i).getUserId());
            assertEquals(post.getId(), capturedFeeds.get(i).getPostId());
            assertEquals(post.getCreatedAt(), capturedFeeds.get(i).getCreatedAt());
        }
    }

    @Test
    void getUserFeeds_shouldReturnFromRedisCache_whenCacheExists() {
        // given
        Long userId = 1L;
        int limit = 10;
        String userFeedKey = "user_feed:" + userId;

        Set<Object> redisResult = new LinkedHashSet<>(Arrays.asList("post11", "post10", "post9", "post8", "post7", "post6", "post5", "post4", "post3", "post2", "post1"));
        when(zSetOperations.reverseRange(userFeedKey, 0, limit)).thenReturn(redisResult);
        when(zSetOperations.score(eq(userFeedKey), eq("post2"))).thenReturn(12345.0);

        // when
        FeedResponse response = feedService.getUserFeeds(userId, null, limit);

        // then
        assertEquals(10, response.getPostIds().size());
        assertTrue(response.isHasMore());
        assertEquals(12345L, response.getNextCursor());
        assertEquals("post2", response.getPostIds().get(9));
        verify(mongoTemplate, never()).find(any(), any());
    }

    @Test
    void getUserFeeds_shouldReturnFromMongoDb_whenCacheIsEmpty() {
        // given
        Long userId = 1L;
        int limit = 10;
        String userFeedKey = "user_feed:" + userId;

        when(zSetOperations.reverseRange(userFeedKey, 0, limit)).thenReturn(Collections.emptySet());

        List<Feed> mongoFeeds = new ArrayList<>();
        for (int i = 0; i < limit + 1; i++) {
            Feed feed = new Feed();
            feed.setUserId(userId);
            feed.setPostId("post" + (i + 1));
            feed.setCreatedAt(System.currentTimeMillis() - i * 1000);
            mongoFeeds.add(feed);
        }
        when(mongoTemplate.find(any(Query.class), eq(Feed.class))).thenReturn(mongoFeeds);

        // when
        FeedResponse response = feedService.getUserFeeds(userId, null, limit);

        // then
        assertEquals(limit, response.getPostIds().size());
        assertTrue(response.isHasMore());
        assertEquals(mongoFeeds.get(limit - 1).getCreatedAt(), response.getNextCursor());
        assertEquals("post10", response.getPostIds().get(9));

        verify(zSetOperations, times(limit + 1)).add(eq(userFeedKey), anyString(), anyDouble());
    }

    @Test
    void getUserFeeds_withCursor_shouldReturnFromRedisCache() {
        // given
        Long userId = 1L;
        Long cursor = 12345L;
        int limit = 5;
        String userFeedKey = "user_feed:" + userId;

        Set<Object> redisResult = new LinkedHashSet<>(Arrays.asList("cursorPost", "post5", "post4", "post3", "post2", "post1"));
        when(zSetOperations.reverseRangeByScore(userFeedKey, cursor.doubleValue(), Double.NEGATIVE_INFINITY, 0, limit + 1))
                .thenReturn(redisResult);
        when(zSetOperations.score(eq(userFeedKey), eq("post1"))).thenReturn(54321.0);

        // when
        FeedResponse response = feedService.getUserFeeds(userId, cursor, limit);

        // then
        assertEquals(limit, response.getPostIds().size());
        //assertTrue(response.isHasMore());
        assertEquals(54321L, response.getNextCursor());
        assertEquals("post1", response.getPostIds().get(4));
        assertFalse(response.getPostIds().contains("cursorPost"));
        verify(mongoTemplate, never()).find(any(), any());
    }
}
