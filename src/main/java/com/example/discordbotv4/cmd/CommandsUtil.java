package com.example.discordbotv4.cmd;

import com.example.discordbotv4.exceptions.NotFoundException;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CommandsUtil {

    private final List<BotCmd> commands;

    public CommandsUtil() {
        List<BotCmd> commands = new ArrayList<>();
        commands.add(
                new BotCmd(
                        CmdEnum.CHANGE_DOTA_ID_CMD,
                        "set_dota_id",
                        Collections.singletonList("id"),
                        "Команда для записи вашего Dota ID в бота"
                )
        );
        commands.add(
                new BotCmd(
                        CmdEnum.CHECK_DOTA_ID_CMD,
                        "check_id",
                        "Команда для проверки вашего Dota ID"
                )
        );
        commands.add(
                new BotCmd(
                        CmdEnum.HELP_CMD,
                        "help",
                        "Команда для получения всех команд бота"
                )
        );
        commands.add(
                new BotCmd(
                        CmdEnum.RANDOM_DOTA_CHARACTER_CMD,
                        "random",
                        "Команда для получения случайного героя из Dota 2. P.S. Если вы добавили в бота свой Dota ID, то будет больше информации"
                )
        );
        commands.add(
                new BotCmd(
                        CmdEnum.MY_STATISTIC_CMD,
                        "stat",
                        "Узнать мою статистику. P.S. Работает только с добавленным Dota ID, команда **!set_dota_id {id}**"
                )
        );
        this.commands = commands;
    }

    public Boolean startWith(MessageCreateEvent event, CmdEnum cmdEnum) {
        return event.getMessageContent().startsWith(getCommand(cmdEnum).getCmd());
    }

    public List<BotCmd> getCommands() {
        return commands;
    }

    public BotCmd getCommand(CmdEnum cmdEnum) {
        return commands
                .stream()
                .filter(cmd -> cmd.getCmdEnum().equals(cmdEnum))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Command not found"));
    }
}
