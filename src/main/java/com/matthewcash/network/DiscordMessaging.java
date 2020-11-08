package com.matthewcash.network;

import java.net.URI;
import java.net.URISyntaxException;

import net.md_5.bungee.api.plugin.Plugin;

public class DiscordMessaging extends Plugin {
    private static DiscordMessaging plugin;
    private WebSocket websocket;

    public static DiscordMessaging getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        getProxy().getPluginManager().registerListener(this, new ChatEvents());

        getProxy().getScheduler().runAsync(DiscordMessaging.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    websocket = new WebSocket(new URI("ws://172.18.0.1:8763"));
                    synchronized (websocket) {
                        websocket.connect();
                        while (true) {
                            try {
                                websocket.wait();

                                Thread.sleep(5000);

                                getLogger().info("WebSocket Reconnecting");
                                websocket.reconnect();
                            } catch (InterruptedException e) {
                                getLogger().warning("WebSocket reconnect timer was interrupted!");
                            }
                        }
                    }

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        getLogger().info("Enabled DiscordMessaging!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled DiscordMessaging!");
    }
}
