package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mysql.Follow;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import jakarta.transaction.Transactional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입

    public FollowService(FollowRepository followRepository, RedisTemplate<String, Object> redisTemplate) {
        this.followRepository = followRepository;
        this.redisTemplate = redisTemplate;
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
        String cacheKey = "followers:" + followingId;

        // check cache data
        List<Long> cachedFollowers = (List<Long>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedFollowers != null) {
            System.out.println("Redis Cache Hit: " + cacheKey);
            return cachedFollowers;
        }

        // if miss cache data, query DB
        System.out.println("Retrieve DB: " + cacheKey);
        List<Long> dbFollowers = followRepository.getFollowerIdsByFollowingId(followingId);

        // save cache data for 5 minutes
        redisTemplate.opsForValue().set(cacheKey, dbFollowers, 5, TimeUnit.MINUTES);

        return dbFollowers;    }
}
