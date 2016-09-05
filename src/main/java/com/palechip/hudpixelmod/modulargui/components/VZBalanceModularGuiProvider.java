package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.EnumChatFormatting;

public class VZBalanceModularGuiProvider extends CoinCounterModularGuiProvider {
    public static enum Type {Negative, Total};
    public static final String NEGATIVE_COINS_DISPLAY_TEXT = EnumChatFormatting.RED + "Coins Spent";
    public static final String TOTAL_COINS_DISPLAY_TEXT = "Balance";

    private Type type = Type.Total;
    private boolean shouldDisplay = false;

    @Override
    public boolean doesMatchForGame(Game game) {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.VAMPIREZ) && !GameDetector.isLobby();
    }

    @Override
    public String content() {
        return getRenderingString();
    }


    @Override
    public void setupNewGame() {
        this.shouldDisplay = false;
        super.setupNewGame();
    }

    public String getRenderingString() {
        if(this.shouldDisplay) {
            switch(this.type) {
                case Negative:
                    // the coins are tracked negative but are displayed positive
                    return NEGATIVE_COINS_DISPLAY_TEXT + (-this.coins);
                case Total:
                    if(this.coins < 0) {
                        return EnumChatFormatting.RED + "" + this.coins;
                    } else {
                        return EnumChatFormatting.GREEN + "" + this.coins;
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
}
