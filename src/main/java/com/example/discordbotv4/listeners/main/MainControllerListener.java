package com.example.discordbotv4.listeners.main;

import com.example.discordbotv4.listeners.ChangeIdListener;
import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.models.DotaPlayerHeroes;
import com.example.discordbotv4.repositories.DotaRepository;
import com.example.discordbotv4.services.MessagingService;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MainControllerListener implements MessageComponentCreateListener {

    private final DotaRepository dotaRepository;

    @Autowired
    public MainControllerListener(DotaRepository dotaRepository) {
        this.dotaRepository = dotaRepository;
    }

    @Override
    public void onComponentCreate(MessageComponentCreateEvent event) {
        MessageComponentInteraction messageComponentInteraction = event.getMessageComponentInteraction();
        String customId = messageComponentInteraction.getCustomId();

        switch (customId) {
            case "randomCharacter":
                randomCharacter(messageComponentInteraction);
                break;
            case "addDotaId":
                addDotaId(messageComponentInteraction);
                break;
        }
    }

    private void addDotaId(MessageComponentInteraction messageComponentInteraction) {
        messageComponentInteraction.createImmediateResponder()
                .addEmbed(
                        MessagingService.builder()
                                .author(messageComponentInteraction.getMessage().get().getAuthor())
                                .title("Инструкция")
                                .description("Для добавления/замены вашего id введите команду \"!changeId {id}\"")
                                .channel(messageComponentInteraction.getChannel().get())
                                .build()
                                .getEmbedBuilder()
                )
                .respond();
    }

    private void randomCharacter(MessageComponentInteraction messageComponentInteraction) {
        try {

            String id = ChangeIdListener.idsMap.get(String.valueOf(messageComponentInteraction.getUser().getId()));

            EmbedBuilder playerHeroesEmbedBuilder = null;

            List<DotaCharacter> characters = dotaRepository.loadAllCharacters();
            int characterIndex = (int) Math.floor(Math.random() * characters.size());
            DotaCharacter dotaCharacter = characters.get(characterIndex);


            if (id != null) {
                List<DotaPlayerHeroes> playerHeroes = dotaRepository.loadPlayerHeroes(id);
                DotaPlayerHeroes playerHero = playerHeroes.stream()
                        .filter(v -> v.getHero_id().equals(String.valueOf(dotaCharacter.getId())))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("Ошибка"));
                Map<String, String> playerHeroesFields = new LinkedHashMap<>();
                playerHeroesFields.put("Всего игр", playerHero.getGames().toString());
                playerHeroesFields.put("Побед", playerHero.getWin().toString());
                Double winPercent = round((double) playerHero.getWin() / playerHero.getGames() * 100);
                playerHeroesFields.put("Процент побед", winPercent + "%");

                playerHeroesFields.put("Игр с", playerHero.getWith_games().toString());
                playerHeroesFields.put("Побед с", playerHero.getWith_win().toString());
                Double winWith = round((double) playerHero.getWith_win() / playerHero.getWith_games() * 100);
                playerHeroesFields.put("Процент побед с", winWith + "%");

                playerHeroesFields.put("Игр против", playerHero.getAgainst_games().toString());
                playerHeroesFields.put("Побед против", playerHero.getAgainst_win().toString());
                Double winAgainst = round((double)playerHero.getAgainst_win() / playerHero.getAgainst_games() * 100);
                playerHeroesFields.put("Процент побед против", winAgainst+ "%");

                playerHeroesEmbedBuilder = MessagingService.builder()
                        .author(messageComponentInteraction.getMessage().get().getAuthor())
                        .title("Твоя статистика на этом персе")
                        .channel(messageComponentInteraction.getChannel().get())
                        .fields(playerHeroesFields)
                        .build()
                        .getEmbedBuilder();
            } else {
                playerHeroesEmbedBuilder = MessagingService.builder()
                        .author(messageComponentInteraction.getMessage().get().getAuthor())
                        .title("Статистики нема")
                        .description("Добавьте id для подробной статистики")
                        .description("Для добавления/замены вашего id введите команду \"!changeId {id}\"")
                        .channel(messageComponentInteraction.getChannel().get())
                        .build()
                        .getEmbedBuilder();
            }

            Map<String, String> fields = new LinkedHashMap<>();
            fields.put("Силы", dotaCharacter.getBase_str().toString());
            fields.put("Ловкости", dotaCharacter.getBase_agi().toString());
            fields.put("Интеллекта", dotaCharacter.getBase_int().toString());
            fields.put("Аттрибут", dotaCharacter.getPrimary_attr());
            fields.put("Брони", dotaCharacter.getBase_armor().toString());
            fields.put("Скорость", dotaCharacter.getMove_speed().toString());

            messageComponentInteraction.createImmediateResponder()
                    .addEmbed(
                            MessagingService.builder()
                                    .author(messageComponentInteraction.getMessage().get().getAuthor())
                                    .title("Твой персонаж")
                                    .description(dotaCharacter.getLocalized_name())
                                    .image("https://api.opendota.com" + dotaCharacter.getImg())
                                    .thumbnail("https://api.opendota.com" + dotaCharacter.getIcon())
                                    .channel(messageComponentInteraction.getChannel().get())
                                    .fields(fields)
                                    .build()
                                    .getEmbedBuilder()
                    )
                    .addEmbed(playerHeroesEmbedBuilder)
                    .respond();

        } catch (Exception error) {
            messageComponentInteraction.createImmediateResponder()
                    .addEmbed(
                            MessagingService.builder()
                                    .author(messageComponentInteraction.getMessage().get().getAuthor())
                                    .title("Ошибка")
                                    .channel(messageComponentInteraction.getChannel().get())
                                    .build()
                                    .getEmbedBuilder()
                    )
                    .respond();
        }
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
