package com.example.gyukimbeonboardingtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"com.example.gyukimbeonboardingtask.domain.mysql", "com.example.gyukimbeonboardingtask.domain.mongodb"})
public class GyukimBeOnboardingTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GyukimBeOnboardingTaskApplication.class, args);
    }
}
