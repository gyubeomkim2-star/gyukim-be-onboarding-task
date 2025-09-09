package com.example.gyukimbeonboardingtask.repository.mysql;

import java.util.List;
import com.example.gyukimbeonboardingtask.domain.mysql.Follow;
import com.example.gyukimbeonboardingtask.domain.mysql.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT followerId FROM Follow WHERE followingId = :followingId")
    List<Long> getFollowerIdsByFollowingId(@Param("followingId") Long followingId);
}
