package com.telemis.exercice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="No more rolls allowed in this frame")
public class NoMoreFrameAllowedException extends RuntimeException {
    
}
