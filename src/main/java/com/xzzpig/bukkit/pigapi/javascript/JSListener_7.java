package com.xzzpig.bukkit.pigapi.javascript;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.*;

public class JSListener_7 implements Listener, com.xzzpig.pigutils.event.Listener {
    public static final JSListener_7 instance = new JSListener_7();

    @EventHandler
    public void onEvent(PlayerKickEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerLeashEntityEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerLevelChangeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerLoginEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerPickupItemEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerPortalEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerRegisterChannelEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerRespawnEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerShearEntityEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerStatisticIncrementEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerTeleportEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerToggleFlightEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerToggleSneakEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerToggleSprintEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerUnleashEntityEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PlayerUnregisterChannelEvent event) {
        JSListener.runScript(event);
    }
}
