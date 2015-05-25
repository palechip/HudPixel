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

import com.palechip.hudpixelmod.HudPixelMod;

public class CoinCounterComponent implements IComponent{
    public static final String COINS_DISPLAY_TEXT = EnumChatFormatting.GOLD + "Coins: ";
    protected int coins;

    public String getRenderingString() {
        return COINS_DISPLAY_TEXT + this.coins;
    }
    
    public void setupNewGame() {
        // reset
        this.coins = 0;
    }
    
    public void onChatMessage(String textMessage, String formattedMessage) {
        // if this is a coin message and it isn't a tip
        if((textMessage.startsWith("+") || textMessage.startsWith("(+")) && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
            this.coins += getCoinsFromMessage(textMessage);
        }
        // the total coin message overwrites the counter (but not guild coins!)
        if(!textMessage.toLowerCase().contains("guild coins") && (textMessage.contains("You earned a total of") || textMessage.contains("For a total of ")) && textMessage.toLowerCase().contains("coins")) {
            try {
                String messageStartingWithCoins = "";
                if(textMessage.contains("You earned a total of")) {
                    messageStartingWithCoins = textMessage.substring(textMessage.indexOf("You earned a total of ") + 22);
                } else {
                    messageStartingWithCoins = textMessage.substring(textMessage.indexOf("For a total of ") + 15);
                }
                String totalCoins = messageStartingWithCoins.substring(0,messageStartingWithCoins.indexOf(" "));
                this.coins = Integer.valueOf(totalCoins.replace(" ", ""));
            } catch (Exception e) {
                HudPixelMod.instance().logInfo("Failed to parse total coin message. Ignoring.");
                e.printStackTrace();
                // we failed getting the coins. Hopefully this never happens.
            }
        }
    }

    public static int getCoinsFromMessage(String message) {
        try {
            String newCoins = message.substring(message.indexOf("+") + 1,message.indexOf(" "));
            return Integer.valueOf(newCoins);
        } catch (Exception e) {
            HudPixelMod.instance().logInfo("Failed to parse coin message. Ignoring.");
            // we failed getting the coins. Hopefully this never happens.
        }
        return 0;
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
    public String getConfigName() {
        return "CoinDisplay";
    }

    @Override
    public String getConfigComment() {
        return "Enable/Disable the Coin Counter in %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }
}
