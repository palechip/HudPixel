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

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.util.EnumChatFormatting;

public class VampireZBalanceComponent extends CoinCounterComponent {
    public static enum Type {Negative, Total};
    public static final String NEGATIVE_COINS_DISPLAY_TEXT = EnumChatFormatting.RED + "Coins Spent: ";
    public static final String TOTAL_COINS_DISPLAY_TEXT = "Balance: ";
    
    private Type type;
    private boolean shouldDisplay = false;
    
    public VampireZBalanceComponent(Type type) {
        this.type = type;
    }
    
    @Override
    public void setupNewGame() {
        this.shouldDisplay = false;
        super.setupNewGame();
    }
    
    @Override
    public String getRenderingString() {
        if(this.shouldDisplay) {
            switch(this.type) {
            case Negative:
                // the coins are tracked negative but are displayed positive
                return NEGATIVE_COINS_DISPLAY_TEXT + (-this.coins);
            case Total:
                if(this.coins < 0) {
                    return EnumChatFormatting.RED + TOTAL_COINS_DISPLAY_TEXT + this.coins;
                } else {
                    return EnumChatFormatting.GREEN + TOTAL_COINS_DISPLAY_TEXT + this.coins;
                }
            }
        }
        return "";
    }
    
    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // Was something purchased ingame?
        if(textMessage.startsWith("[VampireZ]: Purchased ")  && (textMessage.toLowerCase().contains("blood") || textMessage.toLowerCase().contains("gold"))) {
            
            // get the amount
            String amountStr = textMessage.replace("[VampireZ]: Purchased ", "");
            amountStr = amountStr.substring(0, amountStr.indexOf(" "));
            int amount = 0;
            try {
                amount = Integer.valueOf(amountStr);
            } catch (Exception e) {
                HudPixelMod.instance().logInfo("Failed to parse amount out of VampireZ purchase message. Ignoring.");
            }
            
            // get the price
            // I know it's ugly that it's hard-coded but 
            int price = 0;
            // blood
            if(textMessage.toLowerCase().contains("blood")) {
                switch(amount) {
                case 50:
                    price = 45;
                    break;
                case 10:
                    price = 10;
                    break;
                }
            }
            // gold
            else {
                switch(amount) {
                case 1000:
                    price = 800;
                    break;
                case 100:
                    price = 90;
                    break;
                case 10:
                    price = 10;
                    break;
                }
            }
            // subtract from coins
            this.coins -= price;
            // The user spent coins, we need this display
            this.shouldDisplay = true;
        }
        // Let the parent track the positive coins. The second part prevents synchronisation with the total coins.
        if(this.type == Type.Total && !textMessage.contains("You earned a total of")) {
            super.onChatMessage(textMessage, formattedMessage);
        }
    }
    
    @Override
    public String getConfigName() {
        return "Balance";
    }
    
    @Override
    public String getConfigComment() {
        return "Turn on/off the advanced Coin Counter for %game. This one tracks spent coins as well!";
    }
    
    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }
}
