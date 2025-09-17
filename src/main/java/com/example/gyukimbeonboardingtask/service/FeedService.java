package com.example.gyukimbeonboardingtask.service;

import com.example.gyukimbeonboardingtask.domain.mongodb.Feed;
import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.dto.FeedResponse;
import com.example.gyukimbeonboardingtask.repository.mongodb.FeedRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.FollowRepository;
import com.example.gyukimbeonboardingtask.repository.mysql.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedService {
    private static final Logger logger = LoggerFactory.getLogger(FeedService.class);

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public FeedService(FollowRepository followRepository, UserRepository userRepository, FeedRepository feedRepository, RedisTemplate<String, Object> redisTemplate, MongoTemplate mongoTemplate) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
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

    public FeedResponse getUserFeeds(Long userId, Long cursor, int limit) {
        String userFeedKey = "user_feed:" + userId;

        logger.info("Starting feed retrieval for userId={}, cursor={}, limit={}", userId, cursor, limit);
        logger.info("Redis key is: {}", userFeedKey);

        // 1. Check Redis for a feed cache first
        Set<Object> redisResult;
        if (cursor == null) {
            // Initial request without a cursor: fetch the latest posts.
            // We fetch limit + 1 items to determine if more pages exist.
            redisResult = redisTemplate.opsForZSet().reverseRange(userFeedKey, 0, limit);
            logger.info("Initial Redis query. Range [0, {}]", limit);
        } else {
            // Subsequent request with a cursor: fetch posts older than the cursor's timestamp.
            // ZREVRANGEBYSCORE: fetches from 'max' to 'min' score, inclusive.
            // We use the cursor's score as the max value to continue the pagination.
            redisResult = redisTemplate.opsForZSet().reverseRangeByScore(userFeedKey, cursor.doubleValue(), Double.NEGATIVE_INFINITY, 0, limit + 1);
            logger.info("Cursor-based Redis query. Score Range [{}, {}] with limit {}", cursor.doubleValue(), Double.NEGATIVE_INFINITY, limit + 1);
        }

        logger.info("Redis query result (Set<Object>): size={}", redisResult != null ? redisResult.size() : "null");

        List<String> retrievedPostIds = redisResult != null ?
                redisResult.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        logger.info("Post IDs from Redis: {}", retrievedPostIds);

        // 2. Process Redis results if a cache exists
        if (!retrievedPostIds.isEmpty()) {
            logger.info("Found data in Redis cache. Returning Redis data.");

            // For cursor-based queries, the first item is the cursor itself. We need to remove it.
            if (cursor != null) {
                retrievedPostIds.remove(0);
            }

            // Check if more items exist for the next page
            boolean hasMore = retrievedPostIds.size() > limit;
            if (hasMore) {
                retrievedPostIds.remove(retrievedPostIds.size() - 1);
            }

            // Calculate the cursor for the next page
            Long nextCursor = null;
            if (!retrievedPostIds.isEmpty()) {
                Double lastPostScore = redisTemplate.opsForZSet().score(userFeedKey, retrievedPostIds.get(retrievedPostIds.size() - 1));
                if (lastPostScore != null) {
                    nextCursor = lastPostScore.longValue();
                }
            }
            return new FeedResponse(retrievedPostIds, nextCursor, hasMore);
        }

        // 3. Fallback to MongoDB if Redis cache is empty or incomplete
        logger.warn("Redis cache is empty or insufficient. Falling back to MongoDB.");
        Query query = new Query(Criteria.where("userId").is(userId));
        if (cursor != null) {
            query.addCriteria(Criteria.where("createdAt").lt(cursor));
            logger.info("MongoDB query with createdAt < {}", cursor);
        }

        query.with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .limit(limit + 1);

        List<Feed> mongoFeeds = mongoTemplate.find(query, Feed.class);
        logger.info("MongoDB query result: size={}", mongoFeeds.size());

        // Populate Redis cache with results from MongoDB for future requests
        mongoFeeds.forEach(feed -> redisTemplate.opsForZSet().add(userFeedKey, feed.getPostId(), feed.getCreatedAt()));
        logger.info("Cached {} items from MongoDB to Redis.", mongoFeeds.size());

        // Process MongoDB results for the final response
        List<String> mongoPostIds = mongoFeeds.stream()
                .map(Feed::getPostId)
                .collect(Collectors.toList());

        boolean hasMoreFromMongo = mongoPostIds.size() > limit;
        if (hasMoreFromMongo) {
            mongoPostIds.remove(mongoPostIds.size() - 1);
        }

        Long nextCursorFromMongo = null;
        if (!mongoPostIds.isEmpty()) {
            Feed lastFeed = mongoFeeds.get(mongoPostIds.size() - 1);
            nextCursorFromMongo = lastFeed.getCreatedAt();
        }

        return new FeedResponse(mongoPostIds, nextCursorFromMongo, hasMoreFromMongo);
    }}
