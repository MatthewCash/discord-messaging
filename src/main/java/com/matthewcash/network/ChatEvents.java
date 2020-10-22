package com.matthewcash.network;

import java.io.IOException;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.PluginLogger;
import net.md_5.bungee.event.EventHandler;

public class ChatEvents implements Listener {

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        String message = event.getMessage();
        UUID uuid = player.getUniqueId();
        String username = player.getName();

        DiscordMessaging.getPlugin().getProxy().getScheduler().runAsync(DiscordMessaging.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    sendDiscordMessage(message, uuid, username);
                } catch (IOException e) {
                    PluginLogger.getLogger("DiscordMessaging")
                            .severe("An error occurred while sending the WebHook request!");
                    e.printStackTrace();
                }
            }
        });
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
