package com.example.gyukimbeonboardingtask.domain.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FollowIdTest {
    private FollowId followId1;
    private FollowId followId2;
    private FollowId followId3;

    @BeforeEach
    void setUp() {
        followId1 = new FollowId();
        followId1.setFollowerId(1L);
        followId1.setFollowingId(2L);

        followId2 = new FollowId();
        followId2.setFollowerId(1L);
        followId2.setFollowingId(2L);

        followId3 = new FollowId();
        followId3.setFollowerId(2L);
        followId3.setFollowingId(1L);
    }

    @Test
    public void testEquals() {
        assertEquals(followId1, followId2);
        assertEquals(followId2, followId1);
        assertNotEquals(followId1, followId3);
        assertNotEquals(null, followId1);
        assertNotEquals(new Object(), followId1);
    }

    @Test
    public void testHashCode() {
        assertEquals(followId1.hashCode(), followId2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        FollowId followId = new FollowId();
        followId.setFollowerId(1L);
        followId.setFollowingId(2L);

        assertEquals(1L, followId.getFollowerId());
        assertEquals(2L, followId.getFollowingId());
    }

    @Test
    public void testConstructor() {
        FollowId followId = new FollowId();
        assertNull(followId.getFollowerId());
        assertNull(followId.getFollowingId());
    }

    @Test
    public void testCanEqual() {
        FollowId followId = new FollowId();
        assertTrue(followId.canEqual(new FollowId()));
        assertFalse(followId.canEqual(new Object()));
    }
}
