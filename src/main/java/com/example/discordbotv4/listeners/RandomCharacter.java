package com.example.discordbotv4.listeners;

import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.repositories.DotaRepository;
import com.example.discordbotv4.services.MessagingService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class RandomCharacter implements MessageCreateListener {

    private final MessagingService messagingService;
    private final DotaRepository dotaRepository;

    @Autowired
    public RandomCharacter(MessagingService messagingService, DotaRepository dotaRepository) {
        this.messagingService = messagingService;
        this.dotaRepository = dotaRepository;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!random")) {

            try {
                List<DotaCharacter> characters = dotaRepository.loadAllCharacters();
                int characterIndex = (int) Math.floor(Math.random() * characters.size());
                DotaCharacter dotaCharacter = characters.get(characterIndex);

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
            } catch (Exception error) {
                messagingService.sendErrorMessage(event.getMessageAuthor(), event.getChannel());
            }
        }
    }
}
