package com.soybean.admin.notification.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    /**
     * 存储用户会话：userId -> WebSocketSession
     */
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /**
     * 存储会话用户映射：sessionId -> userId
     */
    private static final Map<String, String> sessionUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
            sessionUsers.put(session.getId(), userId);
            log.info("WebSocket connected: userId={}, sessionId={}", userId, session.getId());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userId = sessionUsers.get(session.getId());
        log.debug("WebSocket message received: userId={}, message={}", userId, message.getPayload());

        // 处理心跳消息
        if ("ping".equals(message.getPayload())) {
            session.sendMessage(new TextMessage("pong"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = sessionUsers.remove(session.getId());
        if (userId != null) {
            userSessions.remove(userId);
            log.info("WebSocket disconnected: userId={}, sessionId={}, status={}",
                userId, session.getId(), status);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error: sessionId={}", session.getId(), exception);
        session.close();
    }

    /**
     * 发送消息给指定用户
     */
    public void sendMessageToUser(String userId, Object message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
                log.debug("WebSocket message sent: userId={}, message={}", userId, jsonMessage);
            } catch (IOException e) {
                log.error("Failed to send WebSocket message: userId={}", userId, e);
            }
        }
    }

    /**
     * 广播消息给所有在线用户
     */
    public void broadcastMessage(Object message) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            log.error("Failed to serialize message", e);
            return;
        }

        userSessions.forEach((userId, session) -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    log.error("Failed to broadcast message: userId={}", userId, e);
                }
            }
        });
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(String userId) {
        WebSocketSession session = userSessions.get(userId);
        return session != null && session.isOpen();
    }

    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        return (int) userSessions.values().stream().filter(WebSocketSession::isOpen).count();
    }

    /**
     * 从会话中获取用户ID
     */
    private String getUserIdFromSession(WebSocketSession session) {
        // 从URI参数中获取userId
        String uri = session.getUri().toString();
        if (uri != null) {
            String[] params = uri.split("\\?");
            if (params.length > 1) {
                for (String param : params[1].split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
                        return keyValue[1];
                    }
                }
            }
        }
        return null;
    }
}
