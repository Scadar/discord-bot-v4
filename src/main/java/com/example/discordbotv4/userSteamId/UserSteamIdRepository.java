package com.example.discordbotv4.userSteamId;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSteamIdRepository extends JpaRepository<UserSteamId, Long> {

    Optional<UserSteamId> findByUserId(String userId);

}
