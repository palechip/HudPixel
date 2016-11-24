package com.palechip.hudpixelmod.util;

import com.palechip.hudpixelmod.HudPixelMod;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BansHandler {
    public static volatile Map<String, String> map;
    public static void init() {
        new BanListThread();
    }
    public static void checkForBan() {
        if(Minecraft.getMinecraft().thePlayer != null && map.containsKey(Minecraft.getMinecraft().thePlayer.getName().toLowerCase()))
            Minecraft.getMinecraft().displayGuiScreen(new GuiBlacklisted(map.get(Minecraft.getMinecraft().thePlayer.getName().toLowerCase())));
    }
    private static void load(Properties props) {
        map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            map.put(key.toLowerCase(), value);
            System.out.println(key.toLowerCase() + "=" + value);
        }
    }
    private static class BanListThread extends Thread {
        public BanListThread() {
            setName("Ban List Thread");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("http://eladkay.pw/hudpixel/bans.php");
                Properties props = new Properties();
                try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                    props.load(reader);
                    load(props);
                }
            } catch (IOException e) {
                HudPixelMod.instance().getLOGGER().info("Could not load ban list. Either you're offline or github is down. Nothing to worry about, carry on~");
            }
        }
    }
}
