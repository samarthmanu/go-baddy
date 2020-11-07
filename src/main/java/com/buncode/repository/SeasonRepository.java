package com.buncode.repository;

import com.buncode.model.Season;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends CrudRepository<Season, Long> {

    @Query("SELECT s from Season s WHERE current_time() BETWEEN effective_from AND effective_to")
    Season getCurrentSeason();

}