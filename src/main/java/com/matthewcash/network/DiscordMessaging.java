package com.matthewcash.network;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.java_websocket.enums.ReadyState;
import org.slf4j.Logger;

public class DiscordMessaging {
    private WebSocket websocket;

    public static ProxyServer server;
    public static Logger logger;
    public static DiscordMessaging plugin;
    public static Path dataDirectory;

    @Inject
    public DiscordMessaging(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        DiscordMessaging.server = server;
        DiscordMessaging.logger = logger;
        DiscordMessaging.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws URISyntaxException {
        DiscordMessaging.plugin = this;

        ConfigManager.loadConfig();

        server.getEventManager().register(this, new ChatEvents());

        URI websocketUri = new URI(
            ConfigManager.config.getOrElse("websocket_address", "ws://172.18.0.1:8763"));

        websocket = new WebSocket(websocketUri);
        websocket.connect();

        server.getScheduler()
            .buildTask(plugin, () -> {
                if (websocket.getReadyState() != ReadyState.OPEN) {
                    websocket.reconnect();
                }
            })
            .repeat(5L, TimeUnit.SECONDS)
            .schedule();

        logger.info("Enabled DiscordMessaging!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) throws InterruptedException {
        websocket.close(1001);
    }
}
