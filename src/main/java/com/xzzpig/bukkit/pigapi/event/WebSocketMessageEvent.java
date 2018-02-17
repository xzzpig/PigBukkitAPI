package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;
import com.xzzpig.pigutils.websocket.WebSocket;
import com.xzzpig.pigutils.websocket.server.WebSocketServer;

public class WebSocketMessageEvent extends Event {
    private WebSocket client;
    private String message;
    private WebSocketServer server;

    public WebSocketMessageEvent(WebSocketServer server, WebSocket client, String message) {
        this.server = server;
        this.client = client;
        this.message = message;
    }

    public WebSocket getClient() {
        return client;
    }

    public String getMessage() {
        return message;
    }

    public WebSocketServer getServer() {
        return server;
    }
}
