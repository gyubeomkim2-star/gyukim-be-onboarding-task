package com.example.gyukimbeonboardingtask.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@IdClass(FollowId.class)
@Entity
@Table(name = "follows")
@Getter
@Setter
@NoArgsConstructor
public class Follow {

    @Id
    private Long followerId;

    @Id
    private Long followingId;
}
