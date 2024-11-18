package com.telemis.exercice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.telemis.exercice.models.Frame;

@Repository
public interface FrameRepository extends CrudRepository<Frame, Long> {
    
}
