package com.example.discordbotv4.repositories;

import com.example.discordbotv4.models.DotaCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DotaRepository {

    private final static String dotaCharactersUrl = "https://raw.githubusercontent.com/Scadar/discord-bot-v4/master/src/main/resources/data/characters.json";
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

}
