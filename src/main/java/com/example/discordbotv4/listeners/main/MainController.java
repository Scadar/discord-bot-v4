package com.example.discordbotv4.listeners.main;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
public class MainController implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!menu")) {
            new MessageBuilder()
                    .setContent("Меню управление ботом")
                    .addComponents(
                            ActionRow.of(
                                    Button.primary("randomCharacter", "Случайный персонаж"),
                                    Button.primary("addDotaId", "Как добавить мой id для получения статистики")
                            )
                    )
                    .send(event.getChannel());
        }
    }
}
