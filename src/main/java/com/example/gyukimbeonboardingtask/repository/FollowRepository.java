package com.example.gyukimbeonboardingtask.repository;

import com.example.gyukimbeonboardingtask.domain.Follow;
import com.example.gyukimbeonboardingtask.domain.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
