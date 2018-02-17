package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;
import com.xzzpig.pigutils.websocket.WebSocket;
import com.xzzpig.pigutils.websocket.server.WebSocketServer;

public class WebSocketCloseEvent extends Event {
    private WebSocket client;
    private int code;
    private String reason;
    private boolean remote;
    private WebSocketServer server;

    public WebSocketCloseEvent(WebSocketServer server, WebSocket client, int code, String reason, boolean remote) {
        this.server = server;
        this.client = client;
    }

    public WebSocket getClient() {
        return client;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public WebSocketServer getServer() {
        return server;
    }

    public boolean isRemote() {
        return remote;
    }
}
