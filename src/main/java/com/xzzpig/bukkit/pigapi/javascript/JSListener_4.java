package com.xzzpig.bukkit.pigapi.javascript;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;

public class JSListener_4 implements Listener, com.xzzpig.pigutils.event.Listener {
    public static final JSListener_4 instance = new JSListener_4();

    @EventHandler
    public void onEvent(EntityUnleashEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ExpBottleEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(ExplosionPrimeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(FoodLevelChangeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(FurnaceBurnEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(FurnaceExtractEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(FurnaceSmeltEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(HangingBreakByEntityEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(HangingBreakEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(HangingPlaceEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(HorseJumpEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(InventoryClickEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(InventoryCloseEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(InventoryCreativeEvent event) {
        JSListener.runScript(event);
    }

    @EventHandler
    public void onEvent(InventoryDragEvent event) {
        JSListener.runScript(event);
    }
}
