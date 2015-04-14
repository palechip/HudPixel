package com.palechip.hudpixelmod.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * A little helper to get the username of a given UUID.
 * @author palechip
 */
public class UuidHelper {
    private UuidHelper() {}
    
    private static final String FALLBACK_API = "https://api.razex.de/user/username/";
    private static Gson gson = new Gson();
    private static HashMap<String, String> cache = new HashMap<String, String>();
    
    /**
     * This should only be called asynchronously since it takes about 150ms to complete on my computer.
     * @param uuid the UUID to lookup
     * @return the username or null if it failed
     */
    public static String getUsernameFormUUID(String uuid) {
        // Check the cache first
        if(cache.containsKey(uuid)) {
            return cache.get(uuid);
        }
        
        // For MC 1.7.10 getSessionService is called func_152347_ac()
        String name = Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(uuid), (String)null), false).getName();
        
        // Did it fail?
        if(name == null) {
            HudPixelMod.instance().logWarn("Failed to the username for the UUID. Using fallback API...");
            try {
                // make a request to the fallback API
                URL url = new URL(FALLBACK_API + uuid);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                // convert the result to json
                JsonObject content = gson.fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
                // get the name
                name = content.get("username").getAsString();
            } catch(Exception e) {
                HudPixelMod.instance().logWarn("Failed to use the fallback API for username -> UUID!");
                e.printStackTrace();
            }
        }
        
        // cache the result
        if(name != null) {
            cache.put(uuid, name);
        }
        // and return the result
        return name;
    }
}
