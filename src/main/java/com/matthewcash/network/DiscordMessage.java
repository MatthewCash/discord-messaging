package com.matthewcash.network;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

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
        JsonReader jsonReader = Json.createReader(new StringReader(rawJson));
        JsonObject jsonMessage = jsonReader.readObject();
        return jsonMessage;
    }

    public void sendToChat() {
        Component message = MiniMessage.miniMessage()
            .deserialize(
                "<dark_blue>Discord</dark_blue> <gray><tag></gray> <content>",
                Placeholder.unparsed("tag", tag),
                Placeholder.unparsed("content", content));

        DiscordMessaging.server.sendMessage(message);
    }
}
