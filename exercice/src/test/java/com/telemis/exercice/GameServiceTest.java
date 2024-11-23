package com.telemis.exercice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.telemis.exercice.exceptions.GameNotFoundException;
import com.telemis.exercice.exceptions.NoMoreRollAllowedException;
import com.telemis.exercice.models.Frame;
import com.telemis.exercice.models.Game;
import com.telemis.exercice.models.Rule;
import com.telemis.exercice.models.UserPlayer;
import com.telemis.exercice.repositories.GameRepository;
import com.telemis.exercice.services.GameService;

@SpringBootTest
public class GameServiceTest {
    /* @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    @Test
    public void testCreateGame() {
        // Arrange
        UserPlayer player = new UserPlayer();
        player.setUsername("Test Player");
        Rule rule = new Rule(10, 2, 2, 1, 10, 10);

        Game expectedGame = new Game();
        expectedGame.setRule(rule);
        expectedGame.getPlayers().add(player);

        when(gameRepository.save(any(Game.class))).thenReturn(expectedGame);

        // Act
        Game actualGame = gameService.newGame(rule.getId(), "Test Game");

        // Assert
        assertNotNull(actualGame);
        assertEquals(expectedGame.getRule(), actualGame.getRule());
        assertEquals(1, actualGame.getPlayers().size());
        assertEquals(player, actualGame.getPlayers().get(0));
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void testRoll() {
        // Arrange
        UserPlayer player = new UserPlayer();
        player.setUsername("Test Player");

        Rule rule = new Rule(10, 2, 2, 1, 10, 10);
        Game game = new Game();
        game.setRule(rule);
        game.getPlayers().add(player);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        // Act
        Game updatedGame = gameService.lancer(1L, 5);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(1, updatedGame.getFrames().size());
        Frame frame = updatedGame.getFrames().get(0);
        assertEquals(1, frame.getRolls().size());
        assertEquals(5, frame.getRolls().get(0));
        verify(gameRepository).findById(1L);
        verify(gameRepository).save(game);
    }

    @Test
    public void testRollThrowsExceptionWhenGameNotFound() {
        // Arrange
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFoundException.class, () -> {
            gameService.lancer(152L, 5);
        });
    }

    @Test
    public void testRollThrowsExceptionWhenNoMoreRollsAllowed() {
        // Arrange
        UserPlayer player = new UserPlayer();
        player.setUsername("Test Player");

        Rule rule = new Rule(10, 2, 2, 1, 10, 10);

        Game game = new Game();
        game.setRule(rule);
        game.getPlayers().add(player);
        
        Frame frame = new Frame();
        frame.setPlayer(player);
        frame.getRolls().add(10); // Strike
        frame.getRolls().add(null);
        game.getFrames().add(frame);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));

        // Act & Assert
        assertThrows(NoMoreRollAllowedException.class, () -> {
            gameService.lancer(1L, 5);
        });
    } */
}
