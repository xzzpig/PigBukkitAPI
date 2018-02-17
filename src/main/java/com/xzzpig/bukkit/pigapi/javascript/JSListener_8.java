package com.xzzpig.bukkit.pigapi.javascript;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.server.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.SpawnChangeEvent;

public class JSListener_8 implements Listener, com.xzzpig.pigutils.event.Listener {
    public static final JSListener_8 instance = new JSListener_8();

    @EventHandler
    public void onEvent(PlayerVelocityEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PortalCreateEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PotionSplashEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(PrepareItemEnchantEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ProjectileHitEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ProjectileLaunchEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(RemoteServerCommandEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ServerCommandEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ServerListPingEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ServiceRegisterEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ServiceUnregisterEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SheepDyeWoolEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SheepRegrowWoolEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SignChangeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SlimeSplitEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(SpawnChangeEvent event) {
        JSListener.runScript(event);
    }
}
