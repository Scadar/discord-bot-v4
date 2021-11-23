package com.example.discordbotv4.listeners;

import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.services.MessagingService;
import lombok.SneakyThrows;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class RandomCharacter implements MessageCreateListener {

    private final MessagingService messagingService;

    @Autowired
    public RandomCharacter(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    @SneakyThrows
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!random")) {

            try {
                RestTemplate restTemplate = new RestTemplate();

                List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

                converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
                messageConverters.add(converter);
                restTemplate.setMessageConverters(messageConverters);


                String url = "https://raw.githubusercontent.com/Scadar/discord-bot-v4/master/src/main/resources/data/characters.json";

                DotaCharacter[] characters = restTemplate.getForObject(url, DotaCharacter[].class);

                if (characters == null) {
                    throw new Exception("not found");
                }

                int characterIndex = (int) Math.floor(Math.random() * characters.length);
                DotaCharacter dotaCharacter = characters[characterIndex];

                Map<String, String> fields = new LinkedHashMap<>();
                fields.put("Силы", dotaCharacter.getBase_str().toString());
                fields.put("Ловкости", dotaCharacter.getBase_agi().toString());
                fields.put("Интеллекта", dotaCharacter.getBase_int().toString());
                fields.put("Аттрибут", dotaCharacter.getPrimary_attr());
                fields.put("Брони", dotaCharacter.getBase_armor().toString());
                fields.put("Скорость", dotaCharacter.getMove_speed().toString());

                messagingService.sendMessage(
                        event.getMessageAuthor(),
                        "Твой персонаж",
                        dotaCharacter.getLocalized_name(),
                        null,
                        "https://api.opendota.com" + dotaCharacter.getImg(),
                        event.getChannel(),
                        fields
                );
            } catch (Error error) {
                messagingService.sendMessage(
                        event.getMessageAuthor(),
                        "Твой персонаж",
                        "Ошибка",
                        null,
                        null,
                        event.getChannel(),
                        null
                );
            }
        }
    }
}
