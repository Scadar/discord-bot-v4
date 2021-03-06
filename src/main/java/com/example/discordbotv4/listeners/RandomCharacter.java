package com.example.discordbotv4.listeners;

import com.example.discordbotv4.cmd.CmdEnum;
import com.example.discordbotv4.cmd.CommandsUtil;
import com.example.discordbotv4.exceptions.NotFoundException;
import com.example.discordbotv4.dota.models.DotaCharacter;
import com.example.discordbotv4.dota.models.DotaPlayerHeroes;
import com.example.discordbotv4.dota.DotaRepository;
import com.example.discordbotv4.userSteamId.UserSteamId;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import com.example.discordbotv4.utils.MessageUtil;
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
    private final CommandsUtil commandsUtil;

    @Autowired
    public RandomCharacter(UserSteamIdService userSteamIdService, DotaRepository dotaRepository, CommandsUtil commandsUtil) {
        this.userSteamIdService = userSteamIdService;
        this.dotaRepository = dotaRepository;
        this.commandsUtil = commandsUtil;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (commandsUtil.startWith(event, CmdEnum.RANDOM_DOTA_CHARACTER_CMD)) {
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
                            .orElseThrow(() -> new RuntimeException("????????????"));
                    Map<String, String> playerHeroesFields = new LinkedHashMap<>();
                    playerHeroesFields.put("?????????? ??????", playerHero.getGames().toString());
                    playerHeroesFields.put("??????????", playerHero.getWin().toString());
                    Double winPercent = round((double) playerHero.getWin() / playerHero.getGames() * 100);
                    playerHeroesFields.put("?????????????? ??????????", winPercent + "%");

                    playerHeroesFields.put("?????? ??", playerHero.getWith_games().toString());
                    playerHeroesFields.put("?????????? ??", playerHero.getWith_win().toString());
                    Double winWith = round((double) playerHero.getWith_win() / playerHero.getWith_games() * 100);
                    playerHeroesFields.put("?????????????? ?????????? ??", winWith + "%");

                    playerHeroesFields.put("?????? ????????????", playerHero.getAgainst_games().toString());
                    playerHeroesFields.put("?????????? ????????????", playerHero.getAgainst_win().toString());
                    Double winAgainst = round((double) playerHero.getAgainst_win() / playerHero.getAgainst_games() * 100);
                    playerHeroesFields.put("?????????????? ?????????? ????????????", winAgainst + "%");

                    playerHeroesEmbedBuilder = MessageUtil.getEmbedBuilder(event).setTitle("???????? ???????????????????? ???? ???????? ??????????");
                    MessageUtil.addFields(playerHeroesFields, playerHeroesEmbedBuilder);

                } else {
                    playerHeroesEmbedBuilder = MessageUtil
                            .getEmbedBuilder(event)
                            .setDescription("???????????????? id ?????? ?????????????????? ????????????????????. ?????? ????????????????????/???????????? ???????????? id ?????????????? ?????????????? !setDotaId {id}")
                            .setTitle("???????????????????? ????????");
                }

                Map<String, String> fields = new LinkedHashMap<>();
                fields.put("????????", dotaCharacter.getBase_str().toString());
                fields.put("????????????????", dotaCharacter.getBase_agi().toString());
                fields.put("????????????????????", dotaCharacter.getBase_int().toString());
                fields.put("????????????????", dotaCharacter.getPrimary_attr());
                fields.put("??????????", dotaCharacter.getBase_armor().toString());
                fields.put("????????????????", dotaCharacter.getMove_speed().toString());

                EmbedBuilder characterMessage = MessageUtil
                        .getEmbedBuilder(event)
                        .setTitle("???????? ????????????????")
                        .setDescription(dotaCharacter.getLocalized_name())
                        .setImage("https://api.opendota.com" + dotaCharacter.getImg())
                        .setThumbnail("https://api.opendota.com" + dotaCharacter.getIcon());
                MessageUtil.addFields(fields, characterMessage);

                new MessageBuilder().addEmbed(characterMessage).addEmbed(playerHeroesEmbedBuilder).send(event.getChannel());

            } catch (NotFoundException error) {
                MessageUtil.sendMessage(
                        event,
                        new EmbedBuilder()
                                .setTitle("????????????")
                                .setDescription("???????????? ???????????????? Dota ID")
                );
            } catch (Exception error) {
                MessageUtil.sendBasicErrorMessage(event);
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
