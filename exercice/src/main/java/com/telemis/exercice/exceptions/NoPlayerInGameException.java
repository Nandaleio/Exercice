package com.telemis.exercice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Player not in game")
public class NoPlayerInGameException extends RuntimeException {
    
}
