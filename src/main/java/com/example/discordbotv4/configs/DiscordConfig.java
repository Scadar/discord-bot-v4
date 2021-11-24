package com.example.discordbotv4.configs;

import com.example.discordbotv4.listeners.RandomCharacter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DiscordConfig {

    private final Environment env;
    private final RandomCharacter randomCharacter;

    @Autowired
    public DiscordConfig(Environment env, RandomCharacter rateListener) {
        this.env = env;
        this.randomCharacter = rateListener;
    }

    @Bean
    @ConfigurationProperties(value = "discord-api")
    public DiscordApi discordApi() {

        String token = env.getProperty("TOKEN");

        DiscordApi api = new DiscordApiBuilder()
                .setAllNonPrivilegedIntents()
                .setToken(token)
                .login()
                .join();


        api.addMessageCreateListener(randomCharacter);

        return api;
    }

}