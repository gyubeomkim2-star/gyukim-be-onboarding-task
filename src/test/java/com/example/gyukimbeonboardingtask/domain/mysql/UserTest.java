package com.example.gyukimbeonboardingtask.domain.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUsernameGetterAndSetter() {
        String username = "testUser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testPasswordGetterAndSetter() {
        String password = "testPassword";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testNameGetterAndSetter() {
        String name = "Test Name";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }
}
