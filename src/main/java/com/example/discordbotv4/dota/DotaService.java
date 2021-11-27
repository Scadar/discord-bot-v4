package com.example.discordbotv4.dota;

import com.example.discordbotv4.dota.models.DotaPlayerHeroes;
import com.example.discordbotv4.dota.models.DotaPlayerInfo;
import com.example.discordbotv4.exceptions.NotFoundException;
import com.example.discordbotv4.utils.MessageUtil;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DotaService {
    private final DotaRepository dotaRepository;

    public DotaService(DotaRepository dotaRepository) {
        this.dotaRepository = dotaRepository;
    }

    public List<DotaPlayerHeroes> loadPlayerHeroesOrSendErrorMessage(String id, MessageCreateEvent event) {
        try {
            return dotaRepository.loadPlayerHeroes(id);
        } catch (NotFoundException e) {
            MessageUtil.sendErrorBadId(event);
        } catch (Exception e) {
            MessageUtil.sendBasicErrorMessage(event);
        }
        throw new RuntimeException("");
    }

    public List<RecentMatch> loadRecentMatchesOrSendErrorMessage(String id, MessageCreateEvent event) {
        try {
            return dotaRepository.loadRecentMatch(id);
        } catch (NotFoundException e) {
            MessageUtil.sendErrorBadId(event);
            throw new RuntimeException(e);
        } catch (Exception e) {
            MessageUtil.sendBasicErrorMessage(event);
            throw new RuntimeException(e);
        }
    }

    public DotaPlayerInfo loadDotaPlayerInfoOrSendErrorMessage(String id, MessageCreateEvent event) {
        try {
            return dotaRepository.loadDotaPlayerInfo(id);
        } catch (NotFoundException e) {
            MessageUtil.sendErrorBadId(event);
            throw new RuntimeException(e);
        } catch (Exception e) {
            MessageUtil.sendBasicErrorMessage(event);
            throw new RuntimeException(e);
        }
    }

}
