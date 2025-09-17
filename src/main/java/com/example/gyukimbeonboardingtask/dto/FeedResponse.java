package com.example.gyukimbeonboardingtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private List<String> postIds;
    private Long nextCursor;
    private boolean hasMore;
}
