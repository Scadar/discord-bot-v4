package com.example.discordbotv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class DiscordBotV4Application {

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotV4Application.class, args);
    }

}
