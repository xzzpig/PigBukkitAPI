package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;
import com.xzzpig.pigutils.websocket.WebSocket;
import com.xzzpig.pigutils.websocket.handshake.ClientHandshake;
import com.xzzpig.pigutils.websocket.server.WebSocketServer;

public class WebSocketOpenEvent extends Event {
    private WebSocket client;
    private ClientHandshake handshake;
    private WebSocketServer server;

    public WebSocketOpenEvent(WebSocketServer server, WebSocket client, ClientHandshake handshake) {
        this.server = server;
        this.client = client;
        this.handshake = handshake;
    }

    public WebSocket getClient() {
        return client;
    }

    public ClientHandshake getHandshake() {
        return handshake;
    }

    public WebSocketServer getServer() {
        return server;
    }
}
