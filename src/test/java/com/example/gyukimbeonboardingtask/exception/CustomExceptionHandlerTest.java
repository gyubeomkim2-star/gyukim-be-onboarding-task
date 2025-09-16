package com.example.gyukimbeonboardingtask.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class CustomExceptionHandlerTest {

    private final CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

    @Test
    public void testHandleIllegalArgumentExceptionShouldReturnBadRequestWithErrorMessage() {
        String errorMessage = "Invalid argument";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<APIResponse<Void>> response = customExceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response.getBody());
        assertEquals("400", response.getBody().getCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
