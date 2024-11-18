package com.telemis.exercice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.telemis.exercice.models.Rule;

@Repository
public interface RuleRepository extends CrudRepository<Rule, Long> {
    
}
