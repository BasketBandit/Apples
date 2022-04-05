package com.basketbandit.quizzical.socket;

import com.basketbandit.quizzical.rest.PlaceController;
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
import java.util.Arrays;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);
    private static final ArrayList<WebSocketSession> clients = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if(message.getPayload().startsWith("place:")) {
                String[] data = message.getPayload().substring(6).split(",");
                if(data.length < 3 || !Sanitiser.isNumeric(data[0]) || !Sanitiser.isNumeric(data[1]) || !Sanitiser.isHexadecimal(data[2])) {
                    return;
                }
                PlaceController.image.setRGB(Integer.parseInt(data[0]), Integer.parseInt(data[1]), new Color((int) Long.parseLong(data[2], 16)).getRGB());
                System.out.println(Arrays.toString(data));
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
