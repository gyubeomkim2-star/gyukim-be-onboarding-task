package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.Follow;
import com.example.gyukimbeonboardingtask.repository.FollowRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Transactional
    public void followUser(Long followerId, Long followeeId) {
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followeeId)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followeeId);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followeeId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followeeId)) {
            throw new IllegalArgumentException("팔로우하지 않은 사용자입니다.");
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followeeId);
    }
}
