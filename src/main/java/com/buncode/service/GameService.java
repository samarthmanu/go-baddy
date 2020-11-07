package com.buncode.service;

import com.buncode.model.Game;
import com.buncode.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService implements IGameService {

    @Autowired
    private GameRepository repository;

    @Override
    public List<Game> findAll() {

        return (List<Game>) repository.findAll();
    }

    @Override
    public Optional<Game> findById(Long game_id) {

        return repository.findById(game_id);
    }

    @Override
    public Game save(Game game) {

        return repository.save(game);
    }

}