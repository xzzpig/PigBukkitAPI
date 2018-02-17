package com.xzzpig.bukkit.pigapi.javascript;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class JSListener_1 implements Listener, com.xzzpig.pigutils.event.Listener {
    public static final JSListener_1 instance = new JSListener_1();

    @EventHandler
    public void onEvent(AsyncPlayerChatEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(AsyncPlayerPreLoginEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockBurnEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockCanBuildEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockDamageEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockDispenseEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockFadeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockFormEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockFromToEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockGrowEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockIgniteEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockMultiPlaceEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockPhysicsEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockPistonExtendEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(BlockPistonRetractEvent event) {
        JSListener.runScript(event);
    }
}
