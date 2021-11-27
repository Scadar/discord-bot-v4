package com.example.discordbotv4.listeners;

import com.example.discordbotv4.cmd.CmdEnum;
import com.example.discordbotv4.cmd.CommandsUtil;
import com.example.discordbotv4.utils.MessageUtil;
import com.example.discordbotv4.userSteamId.UserSteamId;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckSteamId implements MessageCreateListener {

    private final UserSteamIdService userSteamIdService;
    private final CommandsUtil commandsUtil;

    public CheckSteamId(UserSteamIdService userSteamIdService, CommandsUtil commandsUtil) {
        this.userSteamIdService = userSteamIdService;
        this.commandsUtil = commandsUtil;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (commandsUtil.startWith(event, CmdEnum.CHECK_DOTA_ID_CMD)) {
            String userId = event.getMessageAuthor().getIdAsString();
            Optional<UserSteamId> optionalUserSteamId = userSteamIdService.findByUserId(userId);

            if(optionalUserSteamId.isPresent()){
                UserSteamId userSteamId = optionalUserSteamId.get();
                MessageUtil.sendMessage(
                        event,
                        new EmbedBuilder()
                                .setTitle("Проверка ID")
                                .setDescription("Ваш ID = " + userSteamId.getDotaId())
                );
            } else {
                MessageUtil.sendMessage(
                        event,
                        new EmbedBuilder()
                                .setTitle("Проверка Id")
                                .setDescription("Ваш ID не установлен, воспользуйтесь командой !setDotaId {id} для установки ID")
                );
            }

        }
    }
}
