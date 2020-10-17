package com.buncode.service;

import com.buncode.model.Game;

import java.util.List;
import java.util.Optional;

public interface IGameService {

    List<Game> findAll();

    Optional<Game> findById(Long game_id);

    Game save(Game game);

}