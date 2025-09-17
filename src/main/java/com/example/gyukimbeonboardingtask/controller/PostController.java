package com.example.gyukimbeonboardingtask.controller;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import com.example.gyukimbeonboardingtask.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    /**@GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }**/

    @GetMapping("/{authorId}")
    public ResponseEntity<List<Post>> getPostsByAuthorId(@PathVariable String authorId) {
        List<Post> posts = postService.getPostsByAuthorId(authorId);
        if (posts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        if (postService.deletePost(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> addLikeToPost(@PathVariable String postId, @RequestBody Map<String, String> payload) {
        String userId = payload.get("userId"); // 요청에서 userId를 추출
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (postService.addLike(postId, userId)) {
            return ResponseEntity.ok().build(); // 성공적으로 좋아요 추가
        } else {
            // 게시글이 없거나 이미 좋아요가 눌러진 경우 (addLike는 중복 시도해도 오류는 아님)
            // 여기서는 게시글이 없는 경우만 404로 처리하도록 가정
            if (postService.getPostById(postId).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 이미 좋아요를 누른 경우 409 Conflict
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> removeLikeFromPost(@PathVariable String postId, @RequestBody Map<String, String> payload) {
        String userId = payload.get("userId"); // 요청에서 userId를 추출
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (postService.removeLike(postId, userId)) {
            return ResponseEntity.noContent().build(); // 성공적으로 좋아요 해제 (204 No Content)
        } else {
            // 게시글이 없거나 좋아요가 눌러져 있지 않은 경우
            if (postService.getPostById(postId).isEmpty()) {
                return ResponseEntity.notFound().build(); // 게시글이 없는 경우 404
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build(); // 좋아요가 이미 해제된 경우 304 Not Modified (선택사항)
        }
    }
}
