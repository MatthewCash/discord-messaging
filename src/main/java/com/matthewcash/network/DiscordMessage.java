package com.matthewcash.network;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.PluginLogger;

public class DiscordMessage {
    String tag;
    String nickname;
    String content;
    String primaryRole;

    public DiscordMessage(JsonObject jsonMessage) {
        this.tag = jsonMessage.getString("tag").toString();
        this.nickname = jsonMessage.get("nickname") == JsonValue.NULL ? "" : jsonMessage.getString("nickname");
        this.content = jsonMessage.getString("content").toString();
        this.primaryRole = jsonMessage.getString("primaryRole").toString();
    }

    public static JsonObject getJSON(String rawJson) {
        PluginLogger.getLogger("test").info(rawJson);
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
