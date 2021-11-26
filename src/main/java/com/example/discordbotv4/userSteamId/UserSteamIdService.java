package com.example.discordbotv4.userSteamId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSteamIdService {

    private final UserSteamIdRepository userSteamIdRepository;

    @Autowired
    public UserSteamIdService(UserSteamIdRepository userSteamIdRepository) {
        this.userSteamIdRepository = userSteamIdRepository;
    }

    public Optional<UserSteamId> findByUserId(String userId) {
        return userSteamIdRepository.findByUserId(userId);
    }

    public UserSteamId addOrReplaceUserSteamId(String userId, String dotaId) {
        Optional<UserSteamId> byUserId = findByUserId(userId);

        UserSteamId userSteamId;
        if (byUserId.isPresent()) {
            userSteamId = byUserId.get();
        } else {
            userSteamId = new UserSteamId();
            userSteamId.setUserId(userId);
        }
        userSteamId.setDotaId(dotaId);
        return userSteamIdRepository.save(userSteamId);
    }
}
