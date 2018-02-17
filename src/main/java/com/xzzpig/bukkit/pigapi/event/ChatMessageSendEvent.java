package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;
import org.bukkit.entity.Player;

public class ChatMessageSendEvent extends Event {
    public final Player from, to;
    public String msg;
    public boolean highlight;

    public ChatMessageSendEvent(String msg, Player from, Player to) {
        this.msg = msg;
        this.from = from;
        this.to = to;
    }

    public String getMessage() {
        return msg;
    }

    public ChatMessageSendEvent setMessage(String msg) {
        this.msg = msg;
        return this;
    }
}
