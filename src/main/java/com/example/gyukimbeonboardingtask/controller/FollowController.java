package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.domain.Follow;
import com.example.gyukimbeonboardingtask.dto.FollowRequest;
import com.example.gyukimbeonboardingtask.repository.FollowRepository;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/v0/users")
public class FollowController {

    private final FollowRepository followRepository;

    public FollowController(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @PostMapping("/{followingId}/follow")
    @Transactional
    public String followUser(@PathVariable Long followingId, @RequestBody FollowRequest request) {
        Long followerId = request.getFollowerId();

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            return "이미 팔로우한 사용자입니다.";
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        followRepository.save(follow);

        return "팔로우 성공!";
    }

    @DeleteMapping("/{followingId}/follow")
    @Transactional
    public String unfollowUser(@PathVariable Long followingId, @RequestBody FollowRequest request) {
        Long followerId = request.getFollowerId();

        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            return "팔로우하지 않은 사용자입니다.";
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        return "언팔로우 성공!";
    }
}
