package com.example.discordbotv4;

import com.example.discordbotv4.listeners.ChangeIdListener;
import com.example.discordbotv4.listeners.DotaTest;
import com.example.discordbotv4.listeners.VoiceConnect;
import com.example.discordbotv4.listeners.main.MainController;
import com.example.discordbotv4.listeners.main.MainControllerListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MainBot {

    private final Environment env;
    private final VoiceConnect voiceConnect;
    private final DotaTest dotaTest;
    private final MainController mainController;
    private final MainControllerListener mainControllerListener;
    private final ChangeIdListener changeIdListener;

    @Autowired
    public MainBot(Environment env, VoiceConnect voiceConnect, DotaTest dotaTest, MainController mainController, MainControllerListener mainControllerListener, ChangeIdListener changeIdListener) {
        this.env = env;
        this.voiceConnect = voiceConnect;
        this.dotaTest = dotaTest;
        this.mainController = mainController;
        this.mainControllerListener = mainControllerListener;
        this.changeIdListener = changeIdListener;
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

        api.addMessageCreateListener(dotaTest);
        api.addMessageCreateListener(voiceConnect);
        api.addMessageCreateListener(changeIdListener);

        api.addMessageCreateListener(mainController);
        api.addMessageComponentCreateListener(mainControllerListener);

        return api;
    }

}