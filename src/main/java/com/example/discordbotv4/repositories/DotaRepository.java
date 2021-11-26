package com.example.discordbotv4.repositories;

import com.example.discordbotv4.exceptions.NotFoundException;
import com.example.discordbotv4.models.DotaCharacter;
import com.example.discordbotv4.models.DotaPlayerHeroes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    private final static String dotaPlayerHeroesUrl = "https://api.opendota.com/api/players/";
    private final RestTemplate restTemplate;

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
    public List<DotaPlayerHeroes> loadPlayerHeroes(String id) throws NotFoundException{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("0467e1af-b9b0-4fde-8c43-8672ebb603e6");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> request = new HttpEntity<String>(headers);
        try {
            ResponseEntity<DotaPlayerHeroes[]> result = restTemplate.exchange(dotaPlayerHeroesUrl + id + "/heroes", HttpMethod.GET,
                    request, DotaPlayerHeroes[].class);
            if (result.getBody() != null) {
                return Arrays.asList(result.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new NotFoundException("dota Id = " + id + " Not Found", e);
        }

    }

}
