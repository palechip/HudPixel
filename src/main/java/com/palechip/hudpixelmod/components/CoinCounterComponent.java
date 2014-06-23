package com.palechip.hudpixelmod.components;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.util.EnumChatFormatting;

public class CoinCounterComponent {
    public static final String COINS_DISPLAY_TEXT = EnumChatFormatting.GOLD + "Coins: ";
    private int coins;

    public String getRenderingString() {
        return COINS_DISPLAY_TEXT + this.coins;
    }
    
    public void setupNewGame() {
        // reset
        this.coins = 0;
    }
    
    public void onChatMessage(String textMessage, String formattedMessage) {
        // if this is a coin message
        if(textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins")) {
            try {
                String newCoins = textMessage.substring(1,textMessage.indexOf(" "));
                this.coins += Integer.valueOf(newCoins);
            } catch (Exception e) {
                HudPixelMod.instance().logInfo("Failed to parse coin message. Ignoring.");
                // we failed getting the coins. Hopefully this never happens.
            }
        }
        // the total coin message overwrites the counter (but not guild coins!
        if(!textMessage.toLowerCase().contains("guild coins") && textMessage.startsWith("You earned a total of") && textMessage.toLowerCase().contains("coins")) {
            try {
                String totalCoins = textMessage.replace("You earned a total of ", "").substring(0,textMessage.indexOf(" "));
                this.coins = Integer.valueOf(totalCoins);
            } catch (Exception e) {
                HudPixelMod.instance().logInfo("Failed to parse total coin message. Ignoring.");
                // we failed getting the coins. Hopefully this never happens.
            }
        }
    }
}
