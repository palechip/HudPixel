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
package com.palechip.hudpixelmod.components;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.util.EnumChatFormatting;

public class WallsKillCounter implements IComponent {
    
    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: " + EnumChatFormatting.RED;
    private static final String ASSISTS_DISPLAY = EnumChatFormatting.AQUA +  "" + EnumChatFormatting.ITALIC +"Assists: " + EnumChatFormatting.DARK_GRAY;
    public static enum KillType {Normal, Assists};
    
    private KillType trackedType;
    private int kills;

    public WallsKillCounter(KillType trackedType) {
        this.trackedType = trackedType;
    }

    @Override
    public void setupNewGame() {
        this.kills = 0;
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
        switch (this.trackedType) {
        case Normal:
            // I hope killed by includes all possible deaths
            // known common messages:
            // was killed by
            // was shot and killed by
            if(textMessage.contains("killed by " + FMLClientHandler.instance().getClient().getSession().getUsername())) {
                this.kills++;
            }
            break;
        case Assists:
            // Assists are displayed using the coin message
            if(textMessage.contains("ASSIST") && textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
                this.kills++;
            }
            break;
        }
    }

    @Override
    public String getRenderingString() {
        switch (trackedType) {
        case Normal:
            return KILL_DISPLAY + this.kills;
        case Assists:
            return ASSISTS_DISPLAY + this.kills;
        }
        return "";
    }

    @Override
    public String getConfigName() {
        // TODO Auto-generated method stub
        return "KillCounter";
    }

    @Override
    public String getConfigComment() {
        return "Enable/Disable the Kill and Assist Counter in %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }

}
