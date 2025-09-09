package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FollowRequest;
import org.springframework.http.ResponseEntity;
import com.example.gyukimbeonboardingtask.service.FollowService; // FollowService import 추가
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{followingId}/follow")
    public ResponseEntity<APIResponse<Void>> followUser(@PathVariable Long followingId, @RequestBody FollowRequest request) {
        Long followerId = request.getFollowerId();
        followService.followUser(followerId, followingId);
        return ResponseEntity.ok(APIResponse.success());
    }

    @DeleteMapping("/{followingId}/follow")
    public ResponseEntity<APIResponse<Void>>  unfollowUser(@PathVariable Long followingId, @RequestBody FollowRequest request) {
        Long followerId = request.getFollowerId();
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok(APIResponse.success());
    }
}