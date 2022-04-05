package com.basketbandit.quizzical.socket;

import com.basketbandit.quizzical.Application;
import com.basketbandit.quizzical.util.Sanitiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);
    private static final ArrayList<WebSocketSession> clients = new ArrayList<>();
    private static final HashMap<String, Color> usedColours = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if(message.getPayload().startsWith("place:")) {
                String[] data = message.getPayload().substring(6).split(",");

                // make sure data is valid (kind of)
                if(data.length < 3 || !Sanitiser.isHexadecimal(data[0] + data[1] + data[2])) {
                    return;
                }

                // update BufferedImage
                Application.image.setRGB(Integer.parseInt(data[0]), Integer.parseInt(data[1]), usedColours.computeIfAbsent(data[2], k -> new Color((int) Long.parseLong(data[2], 16))).getRGB());

                // update all connected clients
                for(WebSocketSession client : clients) {
                    client.sendMessage(message);
                }
            }
        } catch(Exception e) {
            log.error("There was an error handling message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        clients.remove(session);
    }
}
