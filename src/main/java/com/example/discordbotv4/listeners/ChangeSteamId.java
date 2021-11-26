package com.example.discordbotv4.listeners;

import com.example.discordbotv4.services.MessagingService;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ChangeSteamId implements MessageCreateListener {

    private final UserSteamIdService userSteamIdService;

    @Autowired
    public ChangeSteamId(UserSteamIdService userSteamIdService) {
        this.userSteamIdService = userSteamIdService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!setDotaId")) {
            List<String> commands = Arrays.asList(event.getMessageContent().split(" "));
            if (commands.size() != 2) {
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Неверный синтаксис")
                        .description("Для добавления/замены вашего id введите команду \"!setDotaId {id}\"")
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
            } else {
                String id = commands.get(1);
                try {
                    userSteamIdService.addOrReplaceUserSteamId(event.getMessageAuthor().getIdAsString(), id);
                    MessagingService.builder()
                            .author(event.getMessageAuthor())
                            .title("Успешно")
                            .description("Ваш id успешно добавлен")
                            .channel(event.getChannel())
                            .build()
                            .sendMessage();
                }catch (Exception e){
                    MessagingService.sendBasicErrorMessage(event);
                }
            }
        }
    }
}
