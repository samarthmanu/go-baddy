package com.buncode.service;

import com.buncode.model.Player;
import com.buncode.model.PointsConfig;
import com.buncode.repository.PlayerRepository;
import com.buncode.repository.PointsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointsConfigService implements IPointsConfigService {

    @Autowired
    private PointsConfigRepository repository;

    @Override
    public List<PointsConfig> findAll() {
        return (List<PointsConfig>) repository.findAll();
    }

    @Override
    public Optional<PointsConfig> findById(long rule_id) {

        return repository.findById(rule_id);
    }

}