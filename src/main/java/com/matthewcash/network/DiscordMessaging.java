package com.matthewcash.network;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

public class DiscordMessaging {
    private WebSocket websocket;

    public static ProxyServer server;
    public static Logger logger;
    public static DiscordMessaging plugin;

    @Inject
    public DiscordMessaging(ProxyServer server, Logger logger) {
        DiscordMessaging.server = server;
        DiscordMessaging.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        DiscordMessaging.plugin = this;

        server.getEventManager().register(this, new ChatEvents());

        server.getScheduler()
                .buildTask(this, () -> {
                    try {
                        websocket = new WebSocket(new URI("ws://172.18.0.1:8763"));
                        synchronized (websocket) {
                            websocket.connect();
                            while (true) {
                                try {
                                    websocket.wait();

                                    Thread.sleep(5000);

                                    logger.info("WebSocket Reconnecting");
                                    websocket.reconnect();
                                } catch (InterruptedException e) {
                                    // Ignore Interruption
                                }
                            }
                        }

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                })
                .schedule();

        logger.info("Enabled DiscordMessaging!");
    }
}
