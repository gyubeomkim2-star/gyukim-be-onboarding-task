package com.example.gyukimbeonboardingtask.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class FollowId implements Serializable {
    private Long followerId;
    private Long followingId;
}