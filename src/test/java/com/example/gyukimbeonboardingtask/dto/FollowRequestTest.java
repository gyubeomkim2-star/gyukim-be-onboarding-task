package com.example.gyukimbeonboardingtask.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FollowRequestTest {

    @Test
    public void testFollowerIdGetterAndSetter() {
        FollowRequest request = new FollowRequest();
        Long followerId = 1L;

        request.setFollowerId(followerId);

        assertEquals(followerId, request.getFollowerId());
    }
}