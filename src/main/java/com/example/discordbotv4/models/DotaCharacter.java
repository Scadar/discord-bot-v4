package com.example.discordbotv4.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DotaCharacter implements Serializable {

    Long id;
    String name;
    String localized_name;
    String primary_attr;
    String attack_type;
    String[] roles;
    String img;
    String icon;

    Float base_health;
    Float base_health_regen;
    Float base_mana;
    Float base_mana_regen;
    Float base_armor;
    Float base_mr;
    Float base_attack_min;
    Float base_attack_max;
    Long base_str;
    Long base_agi;
    Long base_int;
    Float str_gain;
    Float agi_gain;
    Float int_gain;
    Float attack_range;
    Float projectile_speed;
    Float attack_rate;
    Float move_speed;
    Float turn_rate;
    Boolean cm_enabled;
    Float legs;
    Float hero_id;
    Float turbo_picks;
    Float turbo_wins;
    Float pro_ban;
    Float pro_win;
    Float pro_pick;

}
