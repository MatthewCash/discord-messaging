package com.matthewcash.network;

import java.net.URI;

import javax.json.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocket extends WebSocketClient {
    private static WebSocket connection;

    public static WebSocket getWebSocketConnection() {
        return connection;
    }

    public WebSocket(URI endpoint) {
        super(endpoint);
        connection = this;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        DiscordMessaging.logger.info("Connected to WebSocket!");
    }

    @Override
    public void onMessage(String message) {
        JsonObject jsonMessage = DiscordMessage.getJSON(message);
        DiscordMessage discordMessage = new DiscordMessage(jsonMessage);

        discordMessage.sendToChat();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        DiscordMessaging.logger
            .warn("WebSocket has closed: " + String.valueOf(code) + " " + reason);
    }

    @Override
    public void onError(Exception e) {
        if (!this.isOpen()) {
            DiscordMessaging.logger.error("WebSocket client encountered an error while reconnecting!");
            this.close();
            return;
        }
        DiscordMessaging.logger.error("WebSocket client encountered an error!");
        e.printStackTrace();

    }
}
