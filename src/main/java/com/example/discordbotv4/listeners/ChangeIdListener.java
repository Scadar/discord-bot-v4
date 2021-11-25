package com.example.discordbotv4.listeners;

import com.example.discordbotv4.services.MessagingService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChangeIdListener implements MessageCreateListener {

    public final static Map<String, String> idsMap = new HashMap<>();

    static {
        idsMap.put("222036217678528513", "155931999");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!changeId")) {
            List<String> commands = Arrays.asList(event.getMessageContent().split(" "));
            if (commands.size() != 2) {
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Неверный синтаксис")
                        .description("Для добавления/замены вашего id введите команду \"!changeId {id}\"")
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
            } else {
                String id = commands.get(1);
                idsMap.put(event.getMessageAuthor().getIdAsString(), id);
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Успешно")
                        .description("Ваш id успешно добавлен")
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
                System.out.println(idsMap);
            }
        }
    }
}
