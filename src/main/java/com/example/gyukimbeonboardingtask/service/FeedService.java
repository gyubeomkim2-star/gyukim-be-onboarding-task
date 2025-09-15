package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Feed;
import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.dto.FeedResponse;
import com.example.gyukimbeonboardingtask.repository.mongodb.FeedRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedService {
    private static final Logger logger = LoggerFactory.getLogger(FeedService.class);
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public FeedService(FollowRepository followRepository, UserRepository userRepository, FeedRepository feedRepository, RedisTemplate<String, Object> redisTemplate) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "post-created", groupId = "feed-group")
    public void handlePostCreation(Post post) {
        logger.info("새로운 게시물이 생성되었습니다: {}", post.getTitle());

        String postId = post.getId();
        String postAuthorId = post.getAuthorId();
        String userName = userRepository.getUserById(Long.valueOf(postAuthorId)).getUsername();
        Long postCreatedAt = post.getCreatedAt();

        List<Long> followersOfUser = followRepository.getFollowerIdsByFollowingId(Long.valueOf(postAuthorId));
        logger.info(userName + "'s followers: " + followersOfUser.toString());

        for (Long followerId : followersOfUser) {
            Feed userFeed = new Feed();
            userFeed.setUserId(followerId);
            userFeed.setPostId(postId);
            userFeed.setCreatedAt(postCreatedAt);

            feedRepository.save(userFeed);
        }
    }

    /**
     * 특정 유저의 피드를 커서 기반으로 조회합니다.
     * @param userId 피드를 조회할 사용자 ID
     * @param cursor 이전 조회에서 받은 다음 커서 값 (최초 조회 시 null)
     * @param limit 한 번에 조회할 게시물 수
     * @return FeedResponse (게시물 ID 목록, 다음 커서, 더 많은 데이터 존재 여부)
     */
    public FeedResponse getUserFeed(Long userId, Long cursor, int limit) {
        String userFeedKey = "user_feed:" + userId;

        // Redis Sorted Set에서 게시물 ID 조회
        Set<Object> postIdsSet;
        if (cursor == null) {
            // 최초 조회: 가장 최신 게시물부터 limit+1개 조회 (다음 커서 확인을 위해 1개 더 가져옴)
            postIdsSet = redisTemplate.opsForZSet().reverseRange(userFeedKey, 0, limit);
        } else {
            // 커서 기반 조회: cursor보다 점수(score)가 낮은(오래된) 게시물부터 limit+1개 조회
            // ZREVRANGEBYSCORE key max min LIMIT offset count
            // max: 이전 커서 (exclusive) or 이전 커서 -1 (inclusive)
            // min: -infinity
            postIdsSet = redisTemplate.opsForZSet().reverseRangeByScore(userFeedKey, Double.NEGATIVE_INFINITY, cursor - 1, 0, limit + 1);
        }

        List<Long> retrievedPostIds = postIdsSet != null ?
                postIdsSet.stream()
                        .map(id -> Long.valueOf(id.toString())) // Object -> Long 변환
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        boolean hasMore = false;
        if (retrievedPostIds.size() > limit) {
            hasMore = true;
            retrievedPostIds.remove(retrievedPostIds.size() - 1); // 다음 커서 판단용으로 가져온 1개 제거
        }

        Long nextCursor = null;
        if (!retrievedPostIds.isEmpty()) {
            // 조회된 마지막 게시물의 스코어(타임스탬프)를 다음 커서로 사용
            Double lastPostScore = redisTemplate.opsForZSet().score(userFeedKey, retrievedPostIds.get(retrievedPostIds.size() - 1));
            if (lastPostScore != null) {
                nextCursor = lastPostScore.longValue();
            }
        }

        return new FeedResponse(retrievedPostIds, nextCursor, hasMore);
    }
}
