package com.telemis.exercice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.telemis.exercice.models.UserPlayer;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserPlayer, Long> {
    Optional<UserPlayer> findByUsername(String username);
    boolean existsByUsername(String username);
}