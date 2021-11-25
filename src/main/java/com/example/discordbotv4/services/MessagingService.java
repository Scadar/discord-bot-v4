package com.example.discordbotv4.services;

import lombok.Builder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Builder
public class MessagingService {

    private MessageAuthor author;
    private String title;
    private String description;
    private String footer;
    private String thumbnail;
    private String image;
    private TextChannel channel;
    private Map<String, String> fields;

    public CompletableFuture<Message> sendMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setImage(image)
                .setColor(getRandomColor());

        if (fields != null) {
            fields.forEach(embedBuilder::addInlineField);
        }

        return new MessageBuilder().setEmbed(embedBuilder).send(channel);
    }

    public EmbedBuilder getEmbedBuilder(){
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnail)
                .setImage(image)
                .setColor(getRandomColor());

        if (fields != null) {
            fields.forEach(embedBuilder::addInlineField);
        }

        return embedBuilder;
    }

    private Color getRandomColor(){
        int red = (int) Math.floor(Math.random() * 255);
        int green = (int) Math.floor(Math.random() * 255);
        int blue = (int) Math.floor(Math.random() * 255);

        return new Color(red, green, blue);
    }
}
