package com.palechip.hudpixelmod.util;

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

    static int counter;
    
    private UuidHelper() {}
    
    /**
     * This should only be called asynchronously since it takes about 150ms to complete on my computer.
     * @param uuid the UUID to lookup
     * @return the username or null if it failed
     */
    public static String getUsernameFormUUID(String uuid) {
        if(HudPixelMod.IS_DEBUGGING) {
            long time = System.currentTimeMillis();
            // For MC 1.7.10 getSessionService is called func_152347_ac()
            String name = Minecraft.getMinecraft().func_152347_ac().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(uuid), (String)null), false).getName();
            HudPixelMod.instance().logDebug("Took " + (System.currentTimeMillis() - time) + "ms to process UUID. Counter is at " + counter++);
            return name;
        } else {
         // For MC 1.7.10 getSessionService is called func_152347_ac()
            return Minecraft.getMinecraft().func_152347_ac().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(uuid), (String)null), false).getName();
        }
    }
}
