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

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
/*
    Use @link com.palechip.hudpixelmod.modulargui.components.MWKillCounterModularGuiProvider
 */
@Deprecated
public class MegaWallsKillCounter implements IComponent {
    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: " + EnumChatFormatting.RED;
    private static final String FINAL_KILL_DISPLAY = EnumChatFormatting.BLUE + "Final Kills: " + EnumChatFormatting.RED;
    private static final String ASSISTS_DISPLAY = EnumChatFormatting.AQUA +  "" + EnumChatFormatting.ITALIC +"Assists: " + EnumChatFormatting.DARK_GRAY;
    private static final String FINAL_ASSISTS_DISPLAY = EnumChatFormatting.BLUE +  "" + EnumChatFormatting.ITALIC +"Final Assists: " + EnumChatFormatting.DARK_GRAY;
    private static final String WITHER_COINS_DISPLAY = EnumChatFormatting.GOLD + "Wither Coins: ";
    public static enum KillType {Normal, Final, Assists, Final_Assists, Wither_Coins};

    private KillType trackedType;
    private int kills;
    
    // used for the advanced kill tracking
    private static MegaWallsKillCounter normalKillCounter;

    public MegaWallsKillCounter(KillType type) {
        this.trackedType = type;
        if(type == KillType.Normal) {
            normalKillCounter = this;
        }
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
        // coin message?, not from tipping
        if(textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
            switch (this.trackedType) {
            case Normal:
                // exclude wither rushing reward
                if(!textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL") && !textMessage.contains("Wither Damage")) {
                    this.kills++;
                }
                // some ninja detection for kills over 18
                if(this.kills >= 18 && textMessage.contains("was killed by " + FMLClientHandler.instance().getClient().getSession().getUsername())) {
                    this.kills++;
                }
                break;
            case Final:
                if(!textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                    this.kills++;
                    // for the advanced tracking we must subtract a normal kill
                    // every time it turns out to be a final kill
                    if(normalKillCounter.kills > 18) {
                        normalKillCounter.kills -= 1;
                    }
                }
                break;
            case Assists:
                if(textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL")) {
                    this.kills++;
                }
                break;
            case Final_Assists:
                if(textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                    this.kills++;
                }
                break;
            case Wither_Coins:
                if(textMessage.contains("Wither Damage")) {
                    this.kills += CoinCounterComponent.getCoinsFromMessage(textMessage);
                }
                break;
            }
        }
    }

    @Override
    public String getRenderingString() {
        switch (this.trackedType) {
        case Normal:
            return KILL_DISPLAY + this.kills;
        case Final:
            return FINAL_KILL_DISPLAY + this.kills;
        case Assists:
            return ASSISTS_DISPLAY + this.kills;
        case Final_Assists:
            return FINAL_ASSISTS_DISPLAY + this.kills;
        case Wither_Coins:
            return this.kills > 0 ? WITHER_COINS_DISPLAY + this.kills : "";
        }
        return "";
    }

    @Override
    public String getConfigName() {
        if (this.trackedType == KillType.Wither_Coins) {
            return "WitherCoinDisplay";
        } else {
            return "KillCounter";
        }
    }

    @Override
    public String getConfigComment() {
        if (this.trackedType == KillType.Wither_Coins) {
            return "Turn on/off the counting of coins for Wither Damage in %game.";
        } else {
            return "Enable/Disable the Kill and Assist Counter in %game.";
        }
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }

}
