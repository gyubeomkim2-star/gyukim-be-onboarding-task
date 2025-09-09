package com.example.gyukimbeonboardingtask.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {

    private String code;
    private String message;
    private T data;
    
    public static <T> APIResponse<T> success() {
        return new APIResponse<>("200", "SUCCESS", null);
    }

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>("200", "SUCCESS", data);
    }

    public static <T> APIResponse<T> fail(String message) {
        return new APIResponse<>("400", message, null);
    }
}
