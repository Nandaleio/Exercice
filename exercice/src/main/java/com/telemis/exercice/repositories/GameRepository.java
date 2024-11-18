package com.telemis.exercice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.telemis.exercice.models.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    
}
