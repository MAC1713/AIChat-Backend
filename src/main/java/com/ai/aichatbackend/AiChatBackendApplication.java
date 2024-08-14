package com.ai.aichatbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * @author mac
 */
@Configuration
@SpringBootApplication()
@MapperScan("com.ai.aichatbackend.mapper")
public class AiChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiChatBackendApplication.class, args);
    }

}
