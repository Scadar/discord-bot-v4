package com.example.discordbotv4.dota;

import com.example.discordbotv4.dota.models.DotaCharacter;
import com.example.discordbotv4.dota.models.DotaPlayerHeroes;
import com.example.discordbotv4.dota.models.DotaPlayerInfo;
import com.example.discordbotv4.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DotaRepository {

    private final static String dotaCharactersUrl = "https://raw.githubusercontent.com/Scadar/discord-bot-v4/master/src/main/resources/data/characters.json";
    private final static String dotaPlayerUrl = "https://api.opendota.com/api/players/";
    private final RestTemplate restTemplate;

    @Value("${dota.token}")
    private String dotaToken;

    @Autowired
    public DotaRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("allCharacters")
    public List<DotaCharacter> loadAllCharacters() {
        DotaCharacter[] characters = restTemplate.getForObject(dotaCharactersUrl, DotaCharacter[].class);
        if (characters != null) {
            return Arrays.asList(characters);
        } else {
            return Collections.emptyList();
        }
    }

    @Cacheable("playerHeroes")
    public List<DotaPlayerHeroes> loadPlayerHeroes(String id) throws NotFoundException {
        return loadListData(dotaPlayerUrl + id + "/heroes", ParameterizedTypeReference.forType(DotaPlayerHeroes[].class));
    }

    @Cacheable("dotaPlayerInfo")
    public DotaPlayerInfo loadDotaPlayerInfo(String id) throws NotFoundException {
        return loadData(dotaPlayerUrl + id, ParameterizedTypeReference.forType(DotaPlayerInfo.class));
    }

    public List<RecentMatch> loadRecentMatch(String id) throws NotFoundException {
        return loadListData(dotaPlayerUrl + id + "/recentMatches", ParameterizedTypeReference.forType(RecentMatch[].class));
    }

    private <T> List<T> loadListData(String url, ParameterizedTypeReference<T[]> responseType) throws NotFoundException {
        HttpEntity<String> request = getRequest();

        try {
            ResponseEntity<T[]> result = restTemplate.exchange(url, HttpMethod.GET, request, responseType);
            if (result.getBody() != null) {
                return Arrays.asList(result.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new NotFoundException("Not Found", e);
        }

    }

    private <T> T loadData(String url, ParameterizedTypeReference<T> responseType) throws NotFoundException {
        HttpEntity<String> request = getRequest();

        try {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, request, responseType);
            return result.getBody();
        } catch (Exception e) {
            throw new NotFoundException("Not Found", e);
        }

    }

    private HttpEntity<String> getRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(dotaToken);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        return new HttpEntity<>(headers);
    }

}
