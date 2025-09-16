package com.example.gyukimbeonboardingtask;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GYUKIM FEED SERVICE API")
                        .description("피드서비스 온보딩 프로젝트의 백엔드 API 명세서")
                        .version("1.0.0"));
    }
}
