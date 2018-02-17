package com.xzzpig.bukkit.pigapi.javascript;

import com.xzzpig.bukkit.pigapi.event.*;
import com.xzzpig.pigutils.event.EventHandler;
import com.xzzpig.pigutils.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class JSListener_PigAPI implements Listener {
    public static JSListener_PigAPI instance = new JSListener_PigAPI();

    @EventHandler
    public void onEvent(PluginDisableEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PluginEnableEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SimpleWebServeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(StringMatcherEvent event) {
        JSListener.runScript(event);
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void onEvent(WebSocketCloseEvent event) {
        JSListener.runScript(event);
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void onEvent(WebSocketErrorEvent event) {
        JSListener.runScript(event);
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void onEvent(WebSocketMessageEvent event) {
        JSListener.runScript(event);
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void onEvent(WebSocketOpenEvent event) {
        JSListener.runScript(event);
    }
}
