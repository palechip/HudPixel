package com.palechip.hudpixelmod.detectors;

import com.palechip.hudpixelmod.HudPixelMod;

import cpw.mods.fml.client.FMLClientHandler;

public class HypixelNetworkDetector {
    // saves if the player is online on the Hypixel server
    public static boolean isHypixelNetwork;

    private static final String HYPIXEL_DOMAIN = "hypixel.net";

    /**
     * Checks if the Player is on Hypixel Network.
     */
    public void check() {
        // get the IP of the current server
        // only if there is one
        // func_147104_D returns the ServerData of the current server
        if(FMLClientHandler.instance().getClient().func_147104_D() == null) {
            // Did the player disconnect?
            if(isHypixelNetwork) {
                isHypixelNetwork = false;
                HudPixelMod.instance().logInfo("Disconnected from Hypixel Network");
            }
            return;
        }
        String ip = FMLClientHandler.instance().getClient().func_147104_D().serverIP;
        // if the server ip ends with hypixel.net, it belongs to the Hypixel Network (mc.hypixel.net, test.hypixel.net, maybe mvp.hypixel.net after the new EULA,...)
        if(!isHypixelNetwork && ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {
            isHypixelNetwork = true;
            HudPixelMod.instance().logInfo("Joined Hypixel Network");
        }
        // it can happen that the server data doesn't get null
        else if(isHypixelNetwork && !ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {
            isHypixelNetwork = false;
            HudPixelMod.instance().logInfo("Disconnected from Hypixel Network");
        }
    }
}
