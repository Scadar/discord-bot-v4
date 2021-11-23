package com.example.discordbotv4.listeners;

import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.services.MessagingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Component
public class RandomCharacter implements MessageCreateListener {

    private final MessagingServiceImpl messagingService;

    @Autowired
    public RandomCharacter(MessagingServiceImpl messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    @SneakyThrows
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!random")) {

            RestTemplate restTemplate = new RestTemplate();
            String url
                    = "http://localhost:8080/spring-rest/foos";
            ObjectMapper mapper = new ObjectMapper();
            DotaCharacter[] characters = mapper.readValue(new File("characters.json"), DotaCharacter[].class);
            int characterIndex = (int) Math.floor(Math.random() * characters.length);
            DotaCharacter dotaCharacter = characters[characterIndex];

            messagingService.sendMessage(
                    event.getMessageAuthor(),
                    "Твой персонаж",
                    dotaCharacter.getLocalized_name(),
                    null,
                    "https://api.opendota.com" + dotaCharacter.getImg(),
                    event.getChannel()
            );
        }
    }
}
