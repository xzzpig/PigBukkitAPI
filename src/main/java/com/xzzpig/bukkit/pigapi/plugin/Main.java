package com.xzzpig.bukkit.pigapi.plugin;

import com.xzzpig.bukkit.pigapi.TConfig;
import com.xzzpig.bukkit.pigapi.TDataUtils;
import com.xzzpig.bukkit.pigapi.TPrefix;
import com.xzzpig.bukkit.pigapi.event.*;
import com.xzzpig.bukkit.pigapi.javascript.*;
import com.xzzpig.pigutils.Debuger;
import com.xzzpig.pigutils.TJython;
import com.xzzpig.pigutils.TScript;
import com.xzzpig.pigutils.data.PigData;
import com.xzzpig.pigutils.event.Event;
import com.xzzpig.pigutils.pigsimpleweb.PigSimpleWebServer;
import com.xzzpig.pigutils.websocket.WebSocket;
import com.xzzpig.pigutils.websocket.handshake.ClientHandshake;
import com.xzzpig.pigutils.websocket.server.WebSocketServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main extends JavaPlugin {

    public static SaveThread autoSavePrefix = new SaveThread();

    public static Main self;
    public PigSimpleWebServer webserver;
    public WebSocketServer wsserver;
    private boolean ench, enjs, enws, enwsr, enjy;

    public void enableChatManager() {
        if (ench)
            return;
        getServer().getPluginManager().registerEvents(ChatManager.self, this);
        Vars.chatformat = Vars.config.getString("PigAPI.chatmanager.chatformat",
                "&2</world/></n/></prefix/></colorid/>&r:</message/>");
        getLogger().info("ChatManager启动");
        getLogger().info("聊天格式:" + Vars.chatformat);
        autoSavePrefix.start();
        ench = true;
        Event.registListener(ChatManager.self);
    }

    public void enableJsPlugin() {
        if (enjs)
            return;
        getLogger().info("JsPlugin启动");
        new Thread(()->{
            JSPlugin.freshPluginState();
        }).start();
        ;
        getServer().getPluginManager().registerEvents(JSListener.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_1.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_2.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_3.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_4.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_5.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_6.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_7.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_8.instance, this);
        getServer().getPluginManager().registerEvents(JSListener_9.instance, this);
        Event.registListener(JSListener.instance);
        Event.registListener(JSListener_PigAPI.instance);
        enjs = true;
    }

    public void enableWebServer() {
        if (enwsr)
            return;
        webserver = new PigSimpleWebServer(Vars.web_port);
        try {
            webserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info("SimpleWebServer启动于端口:" + Vars.web_port);
        enwsr = true;
    }

    public void enableWebSocket() {
        if (enws)
            return;
        getLogger().info("WebSocket启动于端口:" + Vars.ws_port);
        wsserver = new WebSocketServer(new InetSocketAddress(Vars.ws_port)) {
            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Event.callEvent(new WebSocketCloseEvent(wsserver, conn, code, reason, remote));
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                Event.callEvent(new WebSocketErrorEvent(wsserver, conn, ex));
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                Event.callEvent(new WebSocketMessageEvent(wsserver, conn, message));

            }

            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                Event.callEvent(new WebSocketOpenEvent(wsserver, conn, handshake));
            }
        };
        wsserver.start();
        enws = true;
    }

    public void enableJython() {
        if (enjy)
            return;
        TJython.libPath = this.getDataFolder().getAbsolutePath() + "/lib/";
        TJython.build();
        if (TScript.getJythonScriptEngine() == null) {
            this.getLogger().info("Jython脚本引擎加载失败");
            return;
        } else {
            this.getLogger().info("Jython脚本引擎加载成功");
        }
        enjy = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return Help.PigAPI.runCommand(Help.PigAPI.new CommandInstance(sender, command, label, args));
    }

    // 插件停用函数
    @Override
    public void onDisable() {
        autoSavePrefix.finish();
        if (webserver != null)
            webserver.stop();
        try {
            TDataUtils.saveData();
            getLogger().info("DataUtils的数据保存成功");
        } catch (IOException e1) {
            e1.printStackTrace();
            getLogger().info("DataUtils的数据保存失败");
        }
        Event.callEvent(new PluginUnLoadEvent());
        getLogger().info(getName() + "插件已被停用 ");
    }

    @Override
    public void onEnable() {
        self = this;
        TPrefix.autoSave = false;
        saveDefaultConfig();
        getLogger().info(getName() + getDescription().getVersion() + "插件已被加载");
        Debuger.debug = false;
        try {
            TDataUtils.loadData();
            getLogger().info("DataUtils的数据读取成功");
        } catch (ClassNotFoundException | IOException e1) {
            e1.printStackTrace();
            getLogger().info("DataUtils的数据读取失败");
        }
        Vars.config = TConfig.getConfigFile("PigAPI", "config.yml");
        Vars.dataFile = new File(getDataFolder(), "data.pigdata");
        try {
            Vars.dataFile.createNewFile();
            Vars.pigData = new PigData(Vars.dataFile);
            Vars.prefix = Vars.pigData.getSub("prefix");
        } catch (Exception e) {
            Vars.pigData = new PigData();
            Vars.prefix = Vars.pigData.getSub("prefix");
            TPrefix.setPrefix("default", "default");
        }
        Vars.enable_chatmanager = Vars.config.getBoolean("PigAPI.enable.chatmanager", true);
        if (Vars.enable_chatmanager) {
            enableChatManager();
        }
        Vars.enable_jsplugin = Vars.config.getBoolean("PigAPI.enable.jsplugin", true);
        if (Vars.enable_jsplugin) {
            enableJsPlugin();
        }
        Vars.enable_websocket = Vars.config.getBoolean("PigAPI.enable.websocket", true);
        if (Vars.enable_websocket) {
            Vars.ws_port = Vars.config.getInt("PigAPI.websocket.port", 10727);
            enableWebSocket();
        }
        Vars.enable_webserver = Vars.config.getBoolean("PigAPI.enable.simplewebserver", true);
        if (Vars.enable_webserver) {
            Vars.web_port = Vars.config.getInt("PigAPI.simplewebserver.port", 80);
            enableWebServer();
        }
        Vars.enbale_jython = Vars.config.getBoolean("PigAPI.enable.jython", true);
        if (Vars.enbale_jython) {
            enableJython();
        }
        Event.callEvent(new PluginLoadEvent());
    }

    static class SaveThread extends Thread {
        private boolean go = true;

        public void finish() {
            go = false;
        }

        @Override
        public void run() {
            while (go) {
                TPrefix.savePrefix();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

