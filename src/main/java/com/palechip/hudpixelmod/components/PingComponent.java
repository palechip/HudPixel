/******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the
 * Hypixel Network.
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

package com.palechip.hudpixelmod.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.util.EnumChatFormatting;

import java.net.UnknownHostException;
import java.util.ArrayList;


public class PingComponent implements IComponent {

    private static final int pingCooldwonMs = 2000;
    private static long nextTimeStamp;
    private static long timeStamp;
    private static long lastValidPing;
    private static OldServerPinger serverPinger = new OldServerPinger();
    private static ArrayList<String> renderingStrings;

    @Override
    public void setupNewGame() {

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onTickUpdate() {

    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {

    }

    @Override
    public String getRenderingString() {

        return getStaticRenderingString();
    }

    public static void updateRenderingString(){
        getStaticRenderingString();
    }

    public static String getStaticRenderingString() {

        timeStamp = System.currentTimeMillis();

        if(timeStamp >= nextTimeStamp){

            nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs;

            //starting external Thread to not block the mainthread
            new Thread("pingThread"){
                @Override
                public void run(){
                    try {
                        serverPinger.ping(Minecraft.getMinecraft().getCurrentServerData());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        String output =
                EnumChatFormatting.WHITE
                        + "Ping: "
                        + lastValidPing
                        + "ms";
        long pingBuffer = Minecraft.getMinecraft().getCurrentServerData().pingToServer;
        if(pingBuffer != lastValidPing && pingBuffer > 0){
            lastValidPing = pingBuffer;

            renderingStrings.clear();
            renderingStrings.add(output);
        }

        return output;

    }

    @Override
    public String getConfigName() {
        return null;
    }

    @Override
    public String getConfigComment() {
        return null;
    }

    @Override
    public boolean getConfigDefaultValue() {
        return false;
    }
}
