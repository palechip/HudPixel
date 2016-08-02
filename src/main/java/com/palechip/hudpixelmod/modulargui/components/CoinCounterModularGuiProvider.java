package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.EnumChatFormatting;

public class CoinCounterModularGuiProvider extends HudPixelModularGuiProvider {

    @Override
    public boolean doesMatchForGame(Game game) {
        if(game == Game.NO_GAME || game.getConfiguration().getModID() == GameType.FOOTBALL.getModID())
            return false;
        else return true;
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
    public boolean showElement() {
        return doesMatchForGame(HudPixelMod.instance().gameDetector.getCurrentGame());
    }

    @Override
    public String content() {
        return coins + "";
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return "You earned a total of " + coins + " coins.";
    }

    public static final String COINS_DISPLAY_TEXT = EnumChatFormatting.GOLD + "Coins";
    protected int coins;


    @Override
    public void setupNewGame() {
        // reset
        this.coins = 0;
    }

    @Override
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

}
