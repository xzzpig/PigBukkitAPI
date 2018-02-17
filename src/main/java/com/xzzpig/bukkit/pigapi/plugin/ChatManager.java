package com.xzzpig.bukkit.pigapi.plugin;

import com.xzzpig.bukkit.pigapi.TMessage;
import com.xzzpig.bukkit.pigapi.TPlayer;
import com.xzzpig.bukkit.pigapi.TStringMatcher;
import com.xzzpig.bukkit.pigapi.event.ChatMessageSendEvent;
import com.xzzpig.pigutils.event.Event;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener, com.xzzpig.pigutils.event.Listener {
    public static ChatManager self = new ChatManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!Vars.enable_chatmanager)
            return;
        String[] formats = TStringMatcher.buildStr(Vars.chatformat, event.getPlayer(), false)
                .replaceAll("</message/>", "^^&r" + event.getMessage() + "^^").split("\n");
        for (int i = 0; i < formats.length; i++) {
            for (Player player : TPlayer.getAllPlayers()) {
                ChatMessageSendEvent eve = new ChatMessageSendEvent(formats[i], event.getPlayer(), player);
                Event.callEvent(eve);
                if (!eve.isCanceled())
                    TMessage.getBy(eve.getMessage(), eve.highlight).send(player);
            }
            System.out.println(formats[i]);
        }
        event.setCancelled(true);
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void atPlayer(ChatMessageSendEvent e) {
        if (e.msg.contains("@" + e.to.getName())) {
            e.setMessage(e.msg.replace("@" + e.to.getName(), ChatColor.AQUA + "@" + e.to.getName() + ChatColor.RESET));
            e.to.playSound(e.to.getLocation(), Sound.LEVEL_UP, 1, 1);
        }
    }

    @com.xzzpig.pigutils.event.EventHandler
    public void deseePlayer(ChatMessageSendEvent e) {
        if (!Help.deseemap.containsKey(e.from.getName()))
            return;
        if (Help.deseemap.get(e.from.getName()).contains(e.to.getName()))
            e.setCanceled(true);
    }
}