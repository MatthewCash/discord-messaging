package com.matthewcash.network;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class DiscordMessage {
    String tag;
    String nickname;
    String content;
    String primaryRole;

    public DiscordMessage(JsonObject jsonMessage) {
        this.tag = jsonMessage.getString("tag");
        this.nickname = jsonMessage.getString("nickname");
        this.content = jsonMessage.getString("content");
        this.primaryRole = jsonMessage.getString("primaryRole");
    }

    public static JsonObject getJSON(String rawJson) {
        JsonReader jsonReader = Json.createReader(new StringReader(rawJson));
        JsonObject jsonMessage = jsonReader.readObject();
        return jsonMessage;
    }

    public void sendToChat() {
        BaseComponent[] message = new ComponentBuilder("Discord ").color(ChatColor.DARK_BLUE).append(tag)
                .color(ChatColor.GRAY).append(" ").append(content).color(ChatColor.WHITE).create();

        DiscordMessaging.getPlugin().getProxy().broadcast(message);
    }
}
