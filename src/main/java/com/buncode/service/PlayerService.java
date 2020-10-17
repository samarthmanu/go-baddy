package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements IPlayerService {

    @Autowired
    private PlayerRepository repository;

    @Override
    public List<Player> findAllValid() {

        return repository.findAllValid();
    }

    @Override
    public List<Player> findAll() {

        return (List<Player>) repository.findAll();
    }

   @Override
    public Player getPlayerByName(String name) {

        return repository.getPlayerByName(name);
    }

    @Override
    public Optional<Player> findById(long player_id) {

        return repository.findById(player_id);
    }

    @Override
    public Player save(Player player) {

        return repository.save(player);
    }

}