package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FeedResponse; // FeedResponse import
import com.example.gyukimbeonboardingtask.service.FeedService; // FeedService import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v0/users")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    /**
     * 특정 유저의 피드를 조회합니다. (커서 기반 페이지네이션)
     * @param userId 피드를 조회할 사용자 ID
     * @param cursor 이전 조회에서 받은 다음 커서 값 (최초 조회 시 null)
     * @param limit 한 번에 조회할 게시물 수 (기본값 10)
     * @return 피드 목록 (게시물 ID 목록, 다음 커서, 더 많은 데이터 존재 여부)
     */
    /*@GetMapping("/{userId}/feed")
    public ResponseEntity<APIResponse<FeedResponse>> getUserFeed(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor, // 다음 페이지 커서
            @RequestParam(defaultValue = "10") int limit // 한 번에 가져올 개수
    ) {
        FeedResponse feedResponse = feedService.getUserFeed(userId, cursor, limit);
        return ResponseEntity.ok(APIResponse.success(feedResponse));
    }*/
}
