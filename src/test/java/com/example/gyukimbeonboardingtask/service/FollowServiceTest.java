package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.Follow;
import com.example.gyukimbeonboardingtask.repository.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        followService = new FollowService(followRepository);
    }

    @Test
    void follow_success() {
        Long followerId = 1L;
        Long followeeId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followeeId)).thenReturn(false);

        followService.followUser(followerId, followeeId);

        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void follow_when_already_followed_then_throw_exception() {
        Long followerId = 1L;
        Long followeeId = 2L;

        when(followRepository.existsByFollowerIdAndFollowingId(followerId, followeeId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(followerId, followeeId);
        });

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void follow_when_same_user_then_throw_exception() {
        Long userId = 1L;

        assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(userId, userId);
        });

        verify(followRepository, never()).existsByFollowerIdAndFollowingId(any(), any());
        verify(followRepository, never()).save(any(Follow.class));
    }
}