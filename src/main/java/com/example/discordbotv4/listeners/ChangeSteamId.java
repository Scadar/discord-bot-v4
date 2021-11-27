package com.example.discordbotv4.listeners;

import com.example.discordbotv4.cmd.CmdEnum;
import com.example.discordbotv4.cmd.CommandsUtil;
import com.example.discordbotv4.utils.MessageUtil;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ChangeSteamId implements MessageCreateListener {

    private final UserSteamIdService userSteamIdService;
    private final CommandsUtil commandsUtil;

    @Autowired
    public ChangeSteamId(UserSteamIdService userSteamIdService, CommandsUtil commandsUtil) {
        this.userSteamIdService = userSteamIdService;
        this.commandsUtil = commandsUtil;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (commandsUtil.startWith(event, CmdEnum.CHANGE_DOTA_ID_CMD)) {
            List<String> commands = Arrays.asList(event.getMessageContent().split(" "));
            if (commands.size() != 2) {
                MessageUtil.sendMessage(
                        event,
                        new EmbedBuilder()
                                .setTitle("Неверный синтаксис")
                                .setDescription("Для добавления/замены вашего id введите команду \"!setDotaId {id}\"")
                );
            } else {
                String id = commands.get(1);
                try {
                    userSteamIdService.addOrReplaceUserSteamId(event.getMessageAuthor().getIdAsString(), id);
                    MessageUtil.sendMessage(
                            event,
                            new EmbedBuilder()
                                    .setTitle("Успешно")
                                    .setDescription("Ваш id успешно добавлен")
                    );
                } catch (Exception e) {
                    MessageUtil.sendBasicErrorMessage(event);
                }
            }
        }
    }
}
