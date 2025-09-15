package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mysql.Follow;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입

    private FollowService followService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        followService = new FollowService(followRepository, redisTemplate);
    }

    @Test
    public void testFollowSuccess() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(false);

        followService.followUser(followerId, followingId);

        verify(followRepository).save(any(Follow.class));
    }

    @Test
    public void testFollowWhenAlreadyFollowedThenThrowException() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(followerId, followingId);
        });

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    public void testFollowWhenSameUserThenThrowException() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(userId, userId);
        });

        verify(followRepository, never()).existsByFollowerIdAndFollowingId(any(), any());
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    public void TestUnfollowSuccess() {
        Long followerId = 1L;
        Long followingId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(true);

        followService.unfollowUser(followerId, followingId);

        verify(followRepository).deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Test
    public void unfollow_when_not_followed_then_throw_exception() {
        Long followerId = 1L;
        Long followeeId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followeeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            followService.unfollowUser(followerId, followeeId);
        });

        verify(followRepository, never()).deleteByFollowerIdAndFollowingId(any(), any());
    }

    @Test
    public void getFollowerIdsByFollowingId_success() {
        Long followingId = 1L;
        List<Long> expectedFollowers = List.of(1L, 2L, 3L);
        String cacheKey = "followers:" + followingId;

        ValueOperations valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(null);
        when(followRepository.getFollowerIdsByFollowingId(followingId)).thenReturn(expectedFollowers);

        List<Long> followerIds = followService.getFollowerIdsByFollowingId(followingId);

        assert followerIds.size() == 3;
        assert followerIds.contains(1L);
        assert followerIds.contains(2L);
        assert followerIds.contains(3L);

        verify(valueOperations).get(cacheKey);
        verify(followRepository).getFollowerIdsByFollowingId(followingId);
        verify(valueOperations).set(cacheKey, expectedFollowers, 5, MINUTES);
    }
}