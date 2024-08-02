package com.ai.aichatbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author mac
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AiChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiChatBackendApplication.class, args);
    }

}
