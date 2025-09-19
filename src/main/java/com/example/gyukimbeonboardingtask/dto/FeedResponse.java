package com.example.gyukimbeonboardingtask.dto;

import com.example.gyukimbeonboardingtask.domain.mongodb.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private List<String> postIds;
    //private List<Post> posts;
    private Long nextCursor;
    private boolean hasMore;
}
