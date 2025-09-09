package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.dto.APIResponse;
import com.example.gyukimbeonboardingtask.dto.FollowRequest;
import org.springframework.http.ResponseEntity;
import com.example.gyukimbeonboardingtask.service.FollowService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/users")
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

    @GetMapping("/{followingId}/followers")
    public ResponseEntity<APIResponse<List<Long>>> getFollowers(@PathVariable Long followingId) {
        List<Long> followers = followService.getFollowerIdsByFollowingId(followingId);

        return ResponseEntity.ok(APIResponse.success(followers));
    }

    @DeleteMapping("/{followingId}/follow")
    public ResponseEntity<APIResponse<Void>>  unfollowUser(@PathVariable Long followingId, @RequestBody FollowRequest request) {
        Long followerId = request.getFollowerId();
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok(APIResponse.success());
    }
}