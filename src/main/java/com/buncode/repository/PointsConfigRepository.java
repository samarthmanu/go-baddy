package com.buncode.repository;

import com.buncode.model.PointsConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsConfigRepository extends CrudRepository<PointsConfig, Long> {

}