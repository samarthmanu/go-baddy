package com.buncode.service;

import com.buncode.model.Player;

import java.util.List;
import java.util.Optional;

public interface IPlayerService {

    List<Player> findAllValid();

    List<Player> findAll();

    Player getPlayerByName(String name);

    Optional<Player> findById(long player_id);

    Player save(Player player);

}