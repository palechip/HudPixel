/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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
