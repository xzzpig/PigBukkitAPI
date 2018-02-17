package com.xzzpig.bukkit.pigapi.event;

import com.xzzpig.pigutils.event.Event;

import java.util.HashMap;

public class StringMatcherEvent extends Event {
    HashMap<String, Object> data;
    String souce;

    public StringMatcherEvent(String souce, HashMap<String, Object> data) {
        this.souce = souce;
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getSouce() {
        return souce;
    }

    public void setSouce(String souce) {
        this.souce = souce;
    }
}
