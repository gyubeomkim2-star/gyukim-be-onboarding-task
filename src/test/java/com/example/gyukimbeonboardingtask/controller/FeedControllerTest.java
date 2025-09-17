package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FeedResponse;
import com.example.gyukimbeonboardingtask.service.FeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedControllerTest {

    @Mock
    private FeedService feedService;

    @InjectMocks
    private FeedController feedController;

    @Test
    public void testGetUserFeed_withoutCursor() {
        // given
        Long userId = 1L;
        int limit = 10;
        List<String> postIds = Arrays.asList("post1", "post2");
        FeedResponse expectedResponse = new FeedResponse(postIds, 12345L, true);

        when(feedService.getUserFeeds(userId, null, limit)).thenReturn(expectedResponse);

        // when
        ResponseEntity<APIResponse<FeedResponse>> response = feedController.getUserFeed(userId, Optional.empty(), limit);

        // then
        verify(feedService).getUserFeeds(userId, null, limit);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
        assertEquals(2, response.getBody().getData().getPostIds().size());
    }

    @Test
    public void testGetUserFeed_withCursor() {
        // given
        Long userId = 1L;
        Optional<Long> cursor = Optional.of(98765L);
        int limit = 5;
        List<String> postIds = Arrays.asList("post3", "post4");
        FeedResponse expectedResponse = new FeedResponse(postIds, 54321L, false);

        when(feedService.getUserFeeds(userId, cursor.get(), limit)).thenReturn(expectedResponse);

        // when
        ResponseEntity<APIResponse<FeedResponse>> response = feedController.getUserFeed(userId, cursor, limit);

        // then
        verify(feedService).getUserFeeds(userId, cursor.get(), limit);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("200", response.getBody().getCode());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
        assertFalse(response.getBody().getData().isHasMore());
    }
}
