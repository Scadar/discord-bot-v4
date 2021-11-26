package com.example.discordbotv4;

import com.example.discordbotv4.listeners.ChangeSteamId;
import com.example.discordbotv4.listeners.CheckSteamId;
import com.example.discordbotv4.listeners.Help;
import com.example.discordbotv4.listeners.RandomCharacter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainBot {

    private final ChangeSteamId changeIdListener;
    private final CheckSteamId checkSteamId;
    private final RandomCharacter randomCharacter;
    private final Help help;

    @Value("${discord.token}")
    private String token;

    @Autowired
    public MainBot(ChangeSteamId changeIdListener, CheckSteamId checkSteamId, RandomCharacter randomCharacter, Help help) {
        this.randomCharacter = randomCharacter;
        this.changeIdListener = changeIdListener;
        this.checkSteamId = checkSteamId;
        this.help = help;
    }

    @Bean
    @ConfigurationProperties(value = "discord-api")
    public DiscordApi discordApi() {

        DiscordApi api = new DiscordApiBuilder()
                .setAllNonPrivilegedIntents()
                .setToken(token)
                .login()
                .join();

        api.addMessageCreateListener(changeIdListener);
        api.addMessageCreateListener(checkSteamId);
        api.addMessageCreateListener(randomCharacter);
        api.addMessageCreateListener(help);

        return api;
    }

}