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
package com.palechip.hudpixelmod.detectors;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraftforge.fml.client.FMLClientHandler;

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
        if(FMLClientHandler.instance().getClient().getCurrentServerData() == null) {
            // Did the player disconnect?
            if(isHypixelNetwork) {
                isHypixelNetwork = false;
                HudPixelMod.instance().logInfo("Disconnected from Hypixel Network");
            }
            return;
        }
        String ip = FMLClientHandler.instance().getClient().getCurrentServerData().serverIP;
        // if the server ip ends with hypixel.net, it belongs to the Hypixel Network (mc.hypixel.net, test.hypixel.net, mvp.hypixel.net, creative.hypixel.net)
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
