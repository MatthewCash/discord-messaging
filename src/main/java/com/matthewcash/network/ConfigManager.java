package com.matthewcash.network;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;

public class ConfigManager {
    public static CommentedFileConfig config = CommentedFileConfig
        .builder(DiscordMessaging.dataDirectory.resolve("config.toml").toFile())
        .defaultData(
            DiscordMessaging.class.getResource(
                "/config.toml"))
        .autosave()
        .preserveInsertionOrder()
        .sync()
        .build();

    public static void loadConfig() {
        DiscordMessaging.dataDirectory.toFile().mkdirs();

        config.load();
    }
}