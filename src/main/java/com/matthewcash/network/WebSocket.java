package com.matthewcash.network;

import java.net.URI;

import javax.json.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import net.md_5.bungee.api.plugin.PluginLogger;

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
        PluginLogger.getLogger("DiscordMessaging").info("Connected to WebSocket!");
    }

    @Override
    public void onMessage(String message) {
        JsonObject jsonMessage = DiscordMessage.getJSON(message);
        DiscordMessage discordMessage = new DiscordMessage(jsonMessage);

        discordMessage.sendToChat();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        PluginLogger.getLogger("DiscordMessaging")
                .warning("WebSocket has closed: " + String.valueOf(code) + " " + reason);

        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void onError(Exception e) {
        if (!this.isOpen()) {
            PluginLogger.getLogger("DiscordMessaging")
                    .severe("WebSocket client encountered an error while reconnecting!");
            this.close();
            return;
        }
        PluginLogger.getLogger("DiscordMessaging").severe("WebSocket client encountered an error!");
        e.printStackTrace();

    }
}
