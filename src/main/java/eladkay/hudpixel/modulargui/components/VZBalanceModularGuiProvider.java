/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package eladkay.hudpixel.modulargui.components;


import eladkay.hudpixel.GameDetector;
import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.util.GameType;
import net.minecraft.util.text.TextFormatting;

public class VZBalanceModularGuiProvider extends CoinCounterModularGuiProvider {
    public static final String NEGATIVE_COINS_DISPLAY_TEXT = TextFormatting.RED + "Coins Spent";
    public static final String TOTAL_COINS_DISPLAY_TEXT = "Balance";

    @ConfigPropertyBoolean(category = CCategory.HUD, id = "vampireZBalance", comment = "The VZ Time Tracker", def = true)
    public static boolean enabled = false;
    private Type type = Type.Total;
    private boolean shouldDisplay = false;

    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.VAMPIREZ);
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public boolean showElement() {
        return super.showElement() && enabled;
    }

    @Override
    public void setupNewGame() {
        this.shouldDisplay = false;
        super.setupNewGame();
    }

    public String getRenderingString() {
        if (this.shouldDisplay) {
            switch (this.type) {
                case Negative:
                    // the coins are tracked negative but are displayed positive
                    return NEGATIVE_COINS_DISPLAY_TEXT + (-this.coins);
                case Total:
                    if (this.coins < 0) {
                        return TextFormatting.RED + "" + this.coins;
                    } else {
                        return TextFormatting.GREEN + "" + this.coins;
                    }
            }
        }
        return "";
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // Was something purchased ingame?
        if (textMessage.startsWith("[VampireZ]: Purchased ") && (textMessage.toLowerCase().contains("blood") || textMessage.toLowerCase().contains("gold"))) {

            // get the amount
            String amountStr = textMessage.replace("[VampireZ]: Purchased ", "");
            amountStr = amountStr.substring(0, amountStr.indexOf(" "));
            int amount = 0;
            try {
                amount = Integer.valueOf(amountStr);
            } catch (Exception e) {
                HudPixelMod.Companion.instance().logInfo("Failed to parse amount out of VampireZ purchase message. Ignoring.");
            }

            // get the price
            // I know it's ugly that it's hard-coded but
            int price = 0;
            // blood
            if (textMessage.toLowerCase().contains("blood")) {
                switch (amount) {
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
                switch (amount) {
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
        if (this.type == Type.Total && !textMessage.contains("You earned a total of")) {
            super.onChatMessage(textMessage, formattedMessage);
        }
    }

    public enum Type {Negative, Total}
}
