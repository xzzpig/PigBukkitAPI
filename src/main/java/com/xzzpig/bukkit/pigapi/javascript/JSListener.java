package com.xzzpig.bukkit.pigapi.javascript;

import com.xzzpig.bukkit.pigapi.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSListener implements Listener, com.xzzpig.pigutils.event.Listener {
    public static final JSListener instance = new JSListener();
    private static final JavaScriptAPI api = new JavaScriptAPI();
    private static HashMap<String, List<File>> scriptmap;
    private Main main;

    private JSListener() {
        main = (Main) Bukkit.getPluginManager().getPlugin("PigAPI");
        loadScript();
    }

    private static ScriptEngine getEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        return manager.getEngineByName("javascript");
    }

    public static void runScript(com.xzzpig.pigutils.event.Event event) {
        if (!scriptmap.containsKey(event.getClass().getSimpleName()))
            return;
        ScriptEngine engine = getEngine();
        engine.put("API", api);
        engine.put("event", event);
        for (File script : scriptmap.get(event.getClass().getSimpleName())) {
            try {
                engine.eval(new FileReader(script));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void runScript(Event event) {
        if (!scriptmap.containsKey(event.getEventName()))
            return;
        ScriptEngine engine = getEngine();
        engine.put("API", api);
        engine.put("event", event);
        for (File script : scriptmap.get(event.getEventName())) {
            try {
                engine.eval(new FileReader(script));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadScript() {
        scriptmap = new HashMap<>();
        File eventFolder = new File(main.getDataFolder(), "scripts");
        if (!eventFolder.exists())
            eventFolder.mkdirs();
        for (File file : eventFolder.listFiles()) {
            if (!file.isDirectory())
                continue;
            String name = file.getName();
            List<File> scriptList = new ArrayList<>();
            for (File script : file.listFiles()) {
                scriptList.add(script);
            }
            scriptmap.put(name, scriptList);
        }
    }

}
