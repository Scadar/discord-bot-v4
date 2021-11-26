package com.example.discordbotv4.listeners;

import com.example.discordbotv4.services.MessagingService;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

@Component
public class Help implements MessageCreateListener {

    private final String description =
        "Пример использования команды с параметром: ***!setDotaId 1234567***\n\n" +
        "**!help**              Команда для получения всех команд бота\n\n" +
        "**!setDotaId {id}**    Команда для записи вашего Dota ID в бота\n\n" +
        "**!checkId**           Команда для проверки вашего Dota ID\n\n" +
        "**!random**            Команда для получения случайного героя из Dota 2. P.S. Если вы добавили в бота свой Dota ID, то будет больше информации";

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!help")) {

            new MessageBuilder()
                    .addEmbed(new EmbedBuilder().setDescription(description).setColor(MessagingService.getRandomColor()))
                    .send(event.getChannel());

        }
    }
}
