package com.example.gyukimbeonboardingtask.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.example.gyukimbeonboardingtask.domain.User;
import com.example.gyukimbeonboardingtask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository);
    }

    @Test
    public void testGetUserByIdWhenUserExistsReturnsUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userController.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetUserByIdWhenUserDoesNotExistReturnsNull() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userController.getUserById(1L);

        assertNull(result);
        verify(userRepository).findById(1L);
    }
}
