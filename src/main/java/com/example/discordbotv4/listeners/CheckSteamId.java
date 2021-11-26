package com.example.discordbotv4.listeners;

import com.example.discordbotv4.services.MessagingService;
import com.example.discordbotv4.userSteamId.UserSteamId;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckSteamId implements MessageCreateListener {

    private final UserSteamIdService userSteamIdService;

    public CheckSteamId(UserSteamIdService userSteamIdService) {
        this.userSteamIdService = userSteamIdService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!checkId")) {
            String userId = event.getMessageAuthor().getIdAsString();
            Optional<UserSteamId> optionalUserSteamId = userSteamIdService.findByUserId(userId);

            if(optionalUserSteamId.isPresent()){
                UserSteamId userSteamId = optionalUserSteamId.get();
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Проверка ID")
                        .description("Ваш ID = " + userSteamId.getDotaId())
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
            } else {
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Проверка Id")
                        .description("Ваш ID не установлен, воспользуйтесь командой !setDotaId {id} для установки ID")
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
            }

        }
    }
}
