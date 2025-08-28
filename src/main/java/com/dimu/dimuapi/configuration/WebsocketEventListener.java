package com.dimu.dimuapi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebsocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String,String> sessionHashMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebsocketConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();

        String userId = sha.getFirstNativeHeader("userId");
        if (userId != null) {
            sessionHashMap.put(userId, sessionId);
            messagingTemplate.convertAndSend("/topic/presence/" + userId, "ONLINE");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String userId = sessionHashMap.get(sessionId);

        if (userId != null) {
            sessionHashMap.remove(sessionId);

            // Broadcast presence as 'offline'
            messagingTemplate.convertAndSend("/topic/presence/" + userId, "OFFLINE");
        }
    }
}

