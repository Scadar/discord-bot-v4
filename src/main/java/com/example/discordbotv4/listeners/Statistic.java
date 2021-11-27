package com.example.discordbotv4.listeners;

import com.example.discordbotv4.cmd.CmdEnum;
import com.example.discordbotv4.cmd.CommandsUtil;
import com.example.discordbotv4.dota.DotaService;
import com.example.discordbotv4.dota.RecentMatch;
import com.example.discordbotv4.dota.models.DotaPlayerInfo;
import com.example.discordbotv4.dota.models.TotalStat;
import com.example.discordbotv4.userSteamId.UserSteamId;
import com.example.discordbotv4.userSteamId.UserSteamIdService;
import com.example.discordbotv4.utils.MessageUtil;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Statistic implements MessageCreateListener {

    private final CommandsUtil commandsUtil;
    private final DotaService dotaService;
    private final UserSteamIdService userSteamIdService;

    @Autowired
    public Statistic(CommandsUtil commandsUtil, DotaService dotaService, UserSteamIdService userSteamIdService) {
        this.commandsUtil = commandsUtil;
        this.dotaService = dotaService;
        this.userSteamIdService = userSteamIdService;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (commandsUtil.startWith(event, CmdEnum.MY_STATISTIC_CMD)) {

            CompletableFuture<Message> loadMessage = new MessageBuilder()
                    .append("Идет загрузка...")
                    .send(event.getChannel());

            UserSteamId userFromDb = userSteamIdService.findByUserIdOrSendErrorMessage(event.getMessageAuthor().getIdAsString(), event);
            List<RecentMatch> recentMatches = dotaService.loadRecentMatchesOrSendErrorMessage(userFromDb.getDotaId(), event);
            DotaPlayerInfo dotaPlayerInfo = dotaService.loadDotaPlayerInfoOrSendErrorMessage(userFromDb.getDotaId(), event);
            List<TotalStat> totalStats = dotaService.loadTotalStatsOrSendErrorMessage(userFromDb.getDotaId(), event);


            loadMessage
                    .thenAccept(message -> {
                        message.edit(
                                new MessageBuilder()
                                        .addEmbed(getSteamEmbed(event, dotaPlayerInfo))
                                        .addEmbed(getAccountEmbed(event, recentMatches))
                                        .addEmbed(getTotalStatEmbed(event, totalStats))
                                        .send(event.getChannel()).toString()
                        );
                    });


        }
    }

    private EmbedBuilder getSteamEmbed(MessageCreateEvent event, DotaPlayerInfo dotaPlayerInfo) {
        return MessageUtil.getEmbedBuilder(event)
                .setTitle("Аккаунт")
                .setImage(dotaPlayerInfo.getProfile().getAvatarfull())
                .setUrl(dotaPlayerInfo.getProfile().getProfileurl())
                .setDescription("Ник: **" + dotaPlayerInfo.getProfile().getPersonaname() + "**")
                .addInlineField("Есть плюс?", dotaPlayerInfo.getProfile().getPlus().toString())
                .addInlineField("Последний вход", dotaPlayerInfo.getProfile().getLast_login().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addInlineField("Steam ID", dotaPlayerInfo.getProfile().getSteamid())
                .addInlineField("Dota ID", dotaPlayerInfo.getProfile().getAccount_id().toString());
    }

    private EmbedBuilder getTotalStatEmbed(MessageCreateEvent event, List<TotalStat> totalStats) {
        EmbedBuilder statEmbed = MessageUtil.getEmbedBuilder(event)
                .setTitle("Общая статистика");
        totalStats.forEach(stat -> {
            statEmbed.addInlineField(stat.getField(), stat.getSum().toString());
        });
        return statEmbed;
    }


    private EmbedBuilder getAccountEmbed(MessageCreateEvent event, List<RecentMatch> recentMatches) {
        int size = recentMatches.size();
        AtomicInteger wins = new AtomicInteger();
        AtomicInteger kills = new AtomicInteger();
        AtomicInteger maxKills = new AtomicInteger(0);
        AtomicInteger deaths = new AtomicInteger();
        AtomicInteger maxDeaths = new AtomicInteger(0);
        AtomicInteger assists = new AtomicInteger();
        AtomicInteger maxAssists = new AtomicInteger(0);
        AtomicInteger gold = new AtomicInteger();
        AtomicInteger maxGold = new AtomicInteger(0);
        AtomicInteger exp = new AtomicInteger();
        AtomicInteger maxExp = new AtomicInteger(0);
        AtomicInteger creeps = new AtomicInteger();
        AtomicInteger maxCreeps = new AtomicInteger(0);
        AtomicInteger heroDamage = new AtomicInteger();
        AtomicInteger maxHeroDamage = new AtomicInteger(0);
        AtomicInteger towerDamage = new AtomicInteger();
        AtomicInteger maxTowerDamage = new AtomicInteger(0);
        AtomicInteger heal = new AtomicInteger();
        AtomicInteger maxHeal = new AtomicInteger();

        recentMatches.forEach(match -> {

            setMax(match.getKills(), maxKills);
            setMax(match.getDeaths(), maxDeaths);
            setMax(match.getAssists(), maxAssists);
            setMax(match.getKills(), maxKills);
            setMax(match.getGold_per_min(), maxGold);
            setMax(match.getXp_per_min(), maxExp);
            setMax(match.getLast_hits(), maxCreeps);
            setMax(match.getHero_damage(), maxHeroDamage);
            setMax(match.getTower_damage(), maxTowerDamage);
            setMax(match.getHero_healing(), maxHeal);

            Long player_slot = match.getPlayer_slot();
            if (player_slot > 0 && player_slot <= 127) {
                if (match.getRadiant_win()) {
                    wins.addAndGet(1);
                }
            } else {
                if (!match.getRadiant_win()) {
                    wins.addAndGet(1);
                }
            }
            kills.addAndGet(match.getKills());
            deaths.addAndGet(match.getDeaths());
            assists.addAndGet(match.getAssists());
            gold.addAndGet(match.getGold_per_min());
            exp.addAndGet(match.getXp_per_min());
            creeps.addAndGet(match.getLast_hits());
            heroDamage.addAndGet(match.getHero_damage());
            towerDamage.addAndGet(match.getTower_damage());
            heal.addAndGet(match.getHero_healing());
        });

        return MessageUtil.getEmbedBuilder(event)
                .setTitle("Статистика за последние 20 игр **Средние/Максимальные**")
                .addField("Доля побед", ((double) wins.get() / (double) size * 100d) + "%")
                .addInlineField("Убийств", kills.get() / size + "/" + maxKills)
                .addInlineField("Смертей", deaths.get() / size + "/" + maxDeaths)
                .addInlineField("Помощь", assists.get() / size + "/" + maxAssists)
                .addInlineField("Золото", gold.get() / size + "/" + maxGold)
                .addInlineField("Опыт/мин", exp.get() / size + "/" + maxExp)
                .addInlineField("Добито крипов", creeps.get() / size + "/" + maxCreeps)
                .addInlineField("Урон по героям", heroDamage.get() / size + "/" + maxHeroDamage)
                .addInlineField("Урон по строениям", towerDamage.get() / size + "/" + maxTowerDamage)
                .addInlineField("Лечение", heal.get() / size + "/" + maxHeal);
    }

    private void setMax(Integer value, AtomicInteger current) {
        if (value > current.get()) {
            current.set(value);
        }
    }
}
