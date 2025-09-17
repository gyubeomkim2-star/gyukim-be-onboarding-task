package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FeedResponse;
import com.example.gyukimbeonboardingtask.service.FeedService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<FeedResponse>> getUserFeed(
            @PathVariable Long userId,
            @RequestParam(required = false) Optional<Long> cursor,
            @RequestParam(defaultValue = "10") int limit
    ) {
        FeedResponse feedResponse = feedService.getUserFeeds(userId, cursor.orElse(null), limit);
        return ResponseEntity.ok(APIResponse.success(feedResponse));
    }
}
