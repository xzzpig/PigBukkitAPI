package com.xzzpig.bukkit.pigapi.javascript;

import com.xzzpig.bukkit.pigapi.TStringMatcher;
import com.xzzpig.bukkit.pigapi.plugin.Main;
import com.xzzpig.pigutils.data.PigData;
import com.xzzpig.pigutils.json.JSONObject;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaScriptAPI {
    private PigData data = new PigData();

    public String buildStr(String str, LivingEntity entity, boolean isInt) {
        return TStringMatcher.buildStr(str, entity, isInt);
    }

    public JSONObject createJsonObject(String source) {
        if (source == null)
            return new JSONObject();
        return new JSONObject(source);
    }

    public List<Object> createList() {
        return new ArrayList<Object>();
    }

    public Map<Object, Object> createMap() {
        return new HashMap<>();
    }

    public PigData createPigData() {
        return new PigData();
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public File getFile(String path) {
        return new File(path);
    }

    public File getPluginFolder() {
        return Main.self.getDataFolder();
    }

    public File getSubFile(File file, String path) {
        return new File(file, path);
    }

    public boolean loadClass(String classpath) {
        try {
            this.getClass();
            Class.forName(classpath);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public void setData(String key, Object value) {
        data.set(key, value);
    }
}
