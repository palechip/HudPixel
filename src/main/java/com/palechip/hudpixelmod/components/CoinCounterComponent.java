package com.palechip.hudpixelmod.components;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.util.EnumChatFormatting;

public class CoinCounterComponent implements IComponent{
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
        // filter game tag (relevant for MW)
        textMessage = textMessage.replace("[" + HudPixelMod.instance().gameDetector.getCurrentGame().getChatTag() + "]: ", "");
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
        if(!textMessage.toLowerCase().contains("guild coins") && textMessage.contains("You earned a total of") && textMessage.toLowerCase().contains("coins")) {
            try {
                String messageStartingWithCoins = textMessage.substring(textMessage.indexOf("You earned a total of ") + 22);
                String totalCoins = messageStartingWithCoins.substring(0,messageStartingWithCoins.indexOf(" "));
                this.coins = Integer.valueOf(totalCoins.replace(" ", ""));
            } catch (Exception e) {
                HudPixelMod.instance().logInfo("Failed to parse total coin message. Ignoring.");
                // we failed getting the coins. Hopefully this never happens.
            }
        }
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
}
