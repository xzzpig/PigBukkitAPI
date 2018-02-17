package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;
import com.xzzpig.pigutils.websocket.WebSocket;
import com.xzzpig.pigutils.websocket.server.WebSocketServer;

public class WebSocketErrorEvent extends Event {
    private WebSocket client;
    private Exception error;
    private WebSocketServer server;

    public WebSocketErrorEvent(WebSocketServer server, WebSocket client, Exception error) {
        this.server = server;
        this.client = client;
        this.error = error;
    }

    public WebSocket getClient() {
        return client;
    }

    public Exception getError() {
        return error;
    }

    public WebSocketServer getServer() {
        return server;
    }
}
