package com.buncode.service;

import com.buncode.model.Season;
import com.buncode.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonService implements ISeasonService {

    @Autowired
    private SeasonRepository repository;


    @Override
    public List<Season> findAll() {
        return (List<Season>) repository.findAll();
    }

    @Override
    public Optional<Season> findById(long season_id) {
        return repository.findById(season_id);
    }

    @Override
    public Season getCurrentSeason() {
        return repository.getCurrentSeason();
    }
}