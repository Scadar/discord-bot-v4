package com.example.discordbotv4.dota;

import lombok.Data;

@Data
public class RecentMatch {
    private Long match_id;
    private Long player_slot;
    private Boolean radiant_win;
    private Long duration;
    private Long gameMode;
    private Long lobby_type;
    private Long hero_id;
    private Long start_time;
    private Long version;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Long skill;
    private Integer xp_per_min;
    private Integer gold_per_min;
    private Integer hero_damage;
    private Integer tower_damage;
    private Integer hero_healing;
    private Integer last_hits;
    private Long lane;
    private Long lane_role;
    private Boolean is_roaming;
    private Long cluster;
    private Long leaver_status;
    private Long party_size;
}
