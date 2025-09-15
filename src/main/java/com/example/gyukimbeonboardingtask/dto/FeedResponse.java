package com.example.gyukimbeonboardingtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private List<Long> postIds; // 피드에 포함될 게시물 ID 목록 (실제로는 Post DTO가 올 수 있음)
    private Long nextCursor;     // 다음 페이지를 조회할 때 사용할 커서 (가장 오래된 게시물의 타임스탬프)
    private boolean hasMore;    // 더 많은 게시물이 있는지 여부
}
