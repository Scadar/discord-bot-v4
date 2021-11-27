package com.example.discordbotv4.dota.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DotaPlayerInfo {
    private Long rank_tier;
    private DotaProfile profile;
}
