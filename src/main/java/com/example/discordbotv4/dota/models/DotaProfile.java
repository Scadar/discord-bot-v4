package com.example.discordbotv4.dota.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DotaProfile {
    private Long account_id;
    private String personaname;
    private String name;
    private Boolean plus;
    private String steamid;
    private String avatarfull;
    private String profileurl;
    private LocalDateTime last_login;
}
