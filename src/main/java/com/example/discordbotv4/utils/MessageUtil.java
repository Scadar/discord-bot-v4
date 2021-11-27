package com.example.discordbotv4.utils;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MessageUtil {

    public static CompletableFuture<Message> sendMessage(MessageCreateEvent event, EmbedBuilder embedBuilder) {
        embedBuilder.setAuthor(event.getMessageAuthor())
                .setTimestampToNow()
                .setColor(getRandomColor());
        return new MessageBuilder().setEmbed(embedBuilder).send(event.getChannel());
    }

    public static EmbedBuilder addFields(Map<String, String> fields, EmbedBuilder embedBuilder) {
        if (fields != null) {
            fields.forEach(embedBuilder::addInlineField);
        }
        return embedBuilder;
    }

    public static EmbedBuilder getEmbedBuilder(MessageCreateEvent event) {

        return new EmbedBuilder()
                .setAuthor(event.getMessageAuthor())
                .setTimestampToNow()
                .setColor(getRandomColor());
    }

    public static Color getRandomColor() {
        int red = (int) Math.floor(Math.random() * 255);
        int green = (int) Math.floor(Math.random() * 255);
        int blue = (int) Math.floor(Math.random() * 255);

        return new Color(red, green, blue);
    }

    public static CompletableFuture<Message> sendBasicErrorMessage(MessageCreateEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(event.getMessageAuthor())
                .setTitle("Произошла ошибка")
                .setDescription("Что-то пошло не так, обратитесь к создателю бота (NULL#3391)")
                .setTimestampToNow()
                .setColor(new Color(255, 0, 0));

        return new MessageBuilder().setEmbed(embedBuilder).send(event.getChannel());
    }

    public static CompletableFuture<Message> sendErrorNotFoundId(MessageCreateEvent event){
        return sendMessage(
                event,
                new EmbedBuilder()
                        .setTitle("Ошибка")
                        .setDescription("Ваш Dota ID не установлен. Воспользуйтесь командой !help")
        );
    }

    public static CompletableFuture<Message> sendErrorBadId(MessageCreateEvent event){
        return sendMessage(
                event,
                new EmbedBuilder()
                        .setTitle("Ошибка")
                        .setDescription("Установлен неверный Dota ID")
        );
    }
}
