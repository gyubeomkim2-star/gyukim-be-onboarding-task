package com.example.gyukimbeonboardingtask.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class APIResponseTest {

    @Test
    public void testSuccess() {
        APIResponse<Object> response = APIResponse.success();
        assertEquals("200", response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testSuccessWithString() {
        String testData = "test";
        APIResponse<String> response = APIResponse.success(testData);
        assertEquals("200", response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertEquals(testData, response.getData());
    }

    @Test
    public void testFail() {
        String errorMessage = "Error occurred";
        APIResponse<Object> response = APIResponse.fail(errorMessage);
        assertEquals("400", response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
    }
}