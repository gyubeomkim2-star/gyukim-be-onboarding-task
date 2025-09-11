package com.example.gyukimbeonboardingtask.domain.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class FollowTest {

    @Test
    public void testGetFollowerId() {
        Follow follow = new Follow();
        follow.setFollowerId(1L);
        assertEquals(1L, follow.getFollowerId());
    }

    @Test
    public void testGetFollowingId() {
        Follow follow = new Follow();
        follow.setFollowingId(2L);
        assertEquals(2L, follow.getFollowingId());
    }

    @Test
    public void testSetFollowerId() {
        Follow follow = new Follow();
        follow.setFollowerId(3L);
        assertEquals(3L, follow.getFollowerId());
    }

    @Test
    public void testSetFollowingId() {
        Follow follow = new Follow();
        follow.setFollowingId(4L);
        assertEquals(4L, follow.getFollowingId());
    }

    @Test
    public void testFollowConstructor() {
        Follow follow = new Follow();
        assertNotNull(follow);
        assertNull(follow.getFollowerId());
        assertNull(follow.getFollowingId());
    }
}
