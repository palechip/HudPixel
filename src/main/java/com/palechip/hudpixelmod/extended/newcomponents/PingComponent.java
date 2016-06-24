/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package com.palechip.hudpixelmod.extended.newcomponents;

import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.util.EnumChatFormatting;

import java.net.UnknownHostException;


public class PingComponent{

    private static final int pingCooldwonMs = 2000;
    private static long nextTimeStamp;
    private static long lastValidPing;
    private static OldServerPinger serverPinger = new OldServerPinger();
    private static String pingString;

    /**
     * A methode that returns the last valid ping and updates it if necessary
     * @return "Ping:" + your last ping in ms
     */
    public static String getStaticRenderingString() {

        // updates the ping if the last ping validation has expired
        if(System.currentTimeMillis() >= nextTimeStamp){
            updatePing();
        }

        // updates the current renderString
        if(Minecraft.getMinecraft().getCurrentServerData() == null || Minecraft.getMinecraft() == null) {
            lastValidPing = 0;
            return pingString = "Ping: Irrelevant";
        }

        if(Minecraft.getMinecraft().getCurrentServerData().pingToServer != lastValidPing
                && Minecraft.getMinecraft().getCurrentServerData().pingToServer > 0){
            lastValidPing = Minecraft.getMinecraft().getCurrentServerData().pingToServer;
            pingString =
                    EnumChatFormatting.WHITE
                            + "Ping: "
                            + lastValidPing
                            + "ms";
        }
        return pingString;

    }

    /**
     * the function who updates your ping. Every ping request is done in a external thread,
     * to not block the mainthread while waiting for the response.
     */
    private static void updatePing(){
        nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs;

        //starting external Thread to not block the mainthread
        new Thread("pingThread"){
            @Override
            public void run(){
                try {
                    if(HypixelNetworkDetector.isHypixelNetwork)
                    serverPinger.ping(Minecraft.getMinecraft().getCurrentServerData());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
