package com.example.discordbotv4.listeners;

import com.example.discordbotv4.exceptions.NotFoundException;
import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.models.DotaPlayerHeroes;
import com.example.discordbotv4.repositories.DotaRepository;
import com.example.discordbotv4.services.MessagingService;
import com.example.discordbotv4.userSteamId.UserSteamId;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RandomCharacter implements MessageCreateListener {

    private final UserSteamIdService userSteamIdService;
    private final DotaRepository dotaRepository;

    @Autowired
    public RandomCharacter(UserSteamIdService userSteamIdService, DotaRepository dotaRepository) {
        this.userSteamIdService = userSteamIdService;
        this.dotaRepository = dotaRepository;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageContent().startsWith("!random")) {
            try {
                String userId = event.getMessageAuthor().getIdAsString();
                Optional<UserSteamId> userSteamId = userSteamIdService.findByUserId(userId);

                EmbedBuilder playerHeroesEmbedBuilder = null;

                List<DotaCharacter> characters = dotaRepository.loadAllCharacters();
                int characterIndex = (int) Math.floor(Math.random() * characters.size());
                DotaCharacter dotaCharacter = characters.get(characterIndex);


                if (userSteamId.isPresent()) {
                    String id = userSteamId.get().getDotaId();
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
                    Double winAgainst = round((double) playerHero.getAgainst_win() / playerHero.getAgainst_games() * 100);
                    playerHeroesFields.put("Процент побед против", winAgainst + "%");

                    playerHeroesEmbedBuilder = MessagingService.builder()
                            .author(event.getMessageAuthor())
                            .title("Твоя статистика на этом персе")
                            .channel(event.getChannel())
                            .fields(playerHeroesFields)
                            .build()
                            .getEmbedBuilder();
                } else {
                    playerHeroesEmbedBuilder = MessagingService.builder()
                            .author(event.getMessageAuthor())
                            .title("Статистики нема")
                            .description("Добавьте id для подробной статистики")
                            .description("Для добавления/замены вашего id введите команду \"!setDotaId {id}\"")
                            .channel(event.getChannel())
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

                new MessageBuilder()
                        .addEmbed(
                                MessagingService.builder()
                                        .author(event.getMessageAuthor())
                                        .title("Твой персонаж")
                                        .description(dotaCharacter.getLocalized_name())
                                        .image("https://api.opendota.com" + dotaCharacter.getImg())
                                        .thumbnail("https://api.opendota.com" + dotaCharacter.getIcon())
                                        .channel(event.getChannel())
                                        .fields(fields)
                                        .build()
                                        .getEmbedBuilder()
                        )
                        .addEmbed(playerHeroesEmbedBuilder)
                        .send(event.getChannel());

            } catch (NotFoundException error){
                MessagingService.builder()
                        .author(event.getMessageAuthor())
                        .title("Ошибка")
                        .description("Указан неверный Dota ID")
                        .channel(event.getChannel())
                        .build()
                        .sendMessage();
            }
            catch (Exception error) {
                MessagingService.sendBasicErrorMessage(event);
            }
        }
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
