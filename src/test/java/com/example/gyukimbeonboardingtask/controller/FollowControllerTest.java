package com.example.gyukimbeonboardingtask.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FollowRequest;
import com.example.gyukimbeonboardingtask.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FollowControllerTest {

    @Mock
    private FollowService followService;

    private FollowController followController;

    @BeforeEach
    public void setUp() {
        followController = new FollowController(followService);
    }

    @Test
    public void testFollowUserShouldReturnSuccessResponse() {
        Long followerId = 1L;
        Long followingId = 2L;
        FollowRequest request = new FollowRequest();
        request.setFollowerId(followerId);

        ResponseEntity<APIResponse<Void>> response = followController.followUser(followingId, request);

        verify(followService).followUser(followerId, followingId);
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());
    }

    @Test
    public void testUnfollowUserShouldReturnSuccessResponse() {
        Long followerId = 1L;
        Long followingId = 2L;
        FollowRequest request = new FollowRequest();
        request.setFollowerId(followerId);

        ResponseEntity<APIResponse<Void>> response = followController.unfollowUser(followingId, request);

        verify(followService).unfollowUser(followerId, followingId);
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());    }

    @Test
    public void testGetFollowersShouldReturnFollowersList() {
        Long followingId = 1L;
        List<Long> expectedFollowers = Arrays.asList(2L, 3L);
        when(followService.getFollowerIdsByFollowingId(followingId)).thenReturn(expectedFollowers);

        ResponseEntity<APIResponse<List<Long>>> response = followController.getFollowers(followingId);

        verify(followService).getFollowerIdsByFollowingId(followingId);
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());
        assertEquals(expectedFollowers, response.getBody().getData());
    }
}
