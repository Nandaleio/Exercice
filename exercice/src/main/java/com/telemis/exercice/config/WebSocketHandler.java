package com.telemis.exercice.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NameNotFoundException;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


	@Override //Not used for now :
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
		
		String receivedMessage = (String) message.getPayload();
		session.sendMessage(new TextMessage("Received: " + receivedMessage));
	}

	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId()); // Remove session on disconnect
    }

	public static void sendMessageToSession(String sessionId, String message) throws Exception {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        } else {
			throw new NameNotFoundException("Session not found or closed for ID: " + sessionId);
        }
    }
}