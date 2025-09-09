package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mysql.Follow;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("본인은 팔로우 할 수 없습니다.");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자입니다.");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("팔로우하지 않은 사용자입니다.");
        }

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    public List<Long> getFollowerIdsByFollowingId(Long followingId) {
        return followRepository.getFollowerIdsByFollowingId(followingId);
    }
}
