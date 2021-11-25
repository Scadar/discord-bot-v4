package com.example.discordbotv4.models;

import lombok.Data;

@Data
public class DotaPlayerHeroes {
    String hero_id;
    Long last_played;
    Long games;
    Long win;
    Long with_games;
    Long with_win;
    Long against_games;
    Long against_win;
}
