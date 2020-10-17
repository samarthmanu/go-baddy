package com.buncode.service;

import com.buncode.model.AdminConfig;
import com.buncode.model.Game;
import com.buncode.repository.AdminConfigRepository;
import com.buncode.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminConfigService implements IAdminConfigService {

    @Autowired
    private AdminConfigRepository repository;

    @Override
    public AdminConfig getAdminConfig() {

        return repository.getAdminConfig();
    }

}