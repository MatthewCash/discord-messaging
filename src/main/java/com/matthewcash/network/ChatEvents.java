package com.matthewcash.network;

import java.io.IOException;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

public class ChatEvents {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        String message = event.getMessage();
        UUID uuid = player.getUniqueId();
        String username = player.getUsername();

        DiscordMessaging.server.getScheduler()
                .buildTask(DiscordMessaging.plugin, () -> {
                    try {
                        sendDiscordMessage(message, uuid, username);
                    } catch (IOException e) {
                        DiscordMessaging.logger.error("An error occurred while sending the WebHook request!");
                        e.printStackTrace();
                    }
                })
                .schedule();
    }

    private static void sendDiscordMessage(String message, UUID uuid, String username) throws IOException {

        JsonObjectBuilder payloadJsonBuilder = Json.createObjectBuilder();

        payloadJsonBuilder.add("content", message);
        payloadJsonBuilder.add("username", username);
        payloadJsonBuilder.add("uuid", uuid.toString());

        String rawJson = payloadJsonBuilder.build().toString();

        WebSocket connection = WebSocket.getWebSocketConnection();

        connection.send(rawJson);
    }
}
