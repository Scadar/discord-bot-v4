package com.example.discordbotv4.userSteamId;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "user_steam_id")
@Data
public class UserSteamId {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_steam_id_id_seq")
    @SequenceGenerator(name = "user_steam_id_id_seq", allocationSize = 1)
    private Long id;
    private String userId;
    private String dotaId;
}
