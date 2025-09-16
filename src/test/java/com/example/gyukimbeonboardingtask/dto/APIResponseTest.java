package com.example.gyukimbeonboardingtask.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void testSetCode() {
        APIResponse<Object> response = new APIResponse<>();
        response.setCode("500");
        assertEquals("500", response.getCode());
    }

    @Test
    public void testSetMessage() {
        APIResponse<Object> response = new APIResponse<>();
        response.setMessage("Test Message");
        assertEquals("Test Message", response.getMessage());
    }

    @Test
    public void testSetData() {
        APIResponse<String> response = new APIResponse<>();
        response.setData("Test Data");
        assertEquals("Test Data", response.getData());
    }

    @Test
    public void testDefaultConstructor() {
        APIResponse<Object> response = new APIResponse<>();
        assertNull(response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }
}
