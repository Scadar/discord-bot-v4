package com.example.discordbotv4.listeners;

import com.example.discordbotv4.cmd.CmdEnum;
import com.example.discordbotv4.cmd.CommandsUtil;
import com.example.discordbotv4.utils.MessageUtil;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Help implements MessageCreateListener {

    private final CommandsUtil commandsUtil;

    @Autowired
    public Help(CommandsUtil commandsUtil) {
        this.commandsUtil = commandsUtil;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (commandsUtil.startWith(event, CmdEnum.HELP_CMD)) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Пример использования команды с параметром: **!set_dota_id 1234567**\n\n");

            commandsUtil.getCommands().forEach(cmd -> {
                stringBuilder
                        .append("**")
                        .append(cmd.getCmd());

                cmd.getParameters().forEach(param -> {
                    stringBuilder
                            .append(" ")
                            .append("{")
                            .append(param)
                            .append("}");
                });

                stringBuilder
                        .append("**")
                        .append(" \t\t ")
                        .append(cmd.getDescription())
                        .append("\n\n");
            });

            MessageUtil.sendMessage(
                    event,
                    new EmbedBuilder()
                            .setTitle("Команды бота Dimochka")
                            .setDescription(stringBuilder.toString())
            );
        }
    }
}
