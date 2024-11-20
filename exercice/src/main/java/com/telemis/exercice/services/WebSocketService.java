package com.telemis.exercice.services;

import org.springframework.stereotype.Service;

import com.telemis.exercice.config.WebSocketHandler;

@Service
public class WebSocketService {

    public void notifyClient(String sessionId, String message) {
        try {
            WebSocketHandler.sendMessageToSession(sessionId, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
