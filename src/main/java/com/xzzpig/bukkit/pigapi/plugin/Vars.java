package com.xzzpig.bukkit.pigapi.plugin;

import com.xzzpig.pigutils.data.PigData;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Vars {
    public static String chatformat;
    public static FileConfiguration config;
    public static File dataFile;
    public static boolean enable_chatmanager, enable_jsplugin, enable_websocket, enable_webserver, enbale_jython;
    public static PigData pigData;
    public static PigData prefix;
    public static int ws_port, web_port;
}