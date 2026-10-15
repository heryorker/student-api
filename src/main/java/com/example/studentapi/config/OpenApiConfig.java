package com.example.studentapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生信息管理 REST API")
                        .description("基于 Spring Boot、MyBatis、MySQL 和 Redis 开发的学生管理接口")
                        .version("1.0.0"));
    }
}