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
import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.modulargui.HudPixelModularGuiProvider;
import eladkay.hudpixel.util.GameType;
import net.minecraft.util.text.TextFormatting;

public class BlitzStarTrackerModularGuiProvider extends HudPixelModularGuiProvider {

    public static final String DISPLAY_MESSAGE = TextFormatting.DARK_GREEN + "Blitz Star";
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "blitzStarTracker", comment = "The Blitz Star Tracker", def = true)
    public static boolean enabled = false;
    private final long DURATION = 60000; // = 60s The blitz star ability only lasts 30s. It's intentionally inaccurate.

    private Phase currentPhase;
    private String owner;
    private String activatedBlitz;
    private long startTime;

    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.BLITZ);
    }

    @Override
    public void setupNewGame() {
        this.currentPhase = Phase.NOT_RELEASED;
        this.activatedBlitz = "";
        this.owner = "";
        this.startTime = 0;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onTickUpdate() {
        // update the time when active
        if (this.currentPhase == Phase.ACTIVE) {
            // expired?
            if (System.currentTimeMillis() - startTime >= DURATION) {
                this.currentPhase = Phase.USED;
            }

        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // filter chat tag
        textMessage = textMessage.replace("[" + GameDetector.getCurrentGameType().getNm() + "]: ", "");
        // hide message
        if (textMessage.contains("The Blitz Star has been hidden in a random chest!")) {
            this.currentPhase = Phase.HIDDEN;
        }
        // somebody found it.
        else if (textMessage.contains("found the Blitz Star!")) {
            this.owner = textMessage.substring(0, textMessage.indexOf(' ') + 1).replace(" ", "");
            this.currentPhase = Phase.FOUND;
        }
        // the holder was killed
        else if (textMessage.contains(this.owner + " was killed")) {
            this.owner = ""; // we could find the killer but it isn't done intentionally.
        }
        // somebody used it
        else if (textMessage.contains(" BLITZ! ")) {
            // update the owner
            this.owner = textMessage.substring(0, textMessage.indexOf(' ')).replace(" ", "");
            this.startTime = System.currentTimeMillis();
            this.activatedBlitz = textMessage.substring(textMessage.indexOf('!') + 2).replace(" ", "");
            this.currentPhase = Phase.ACTIVE;
        }
        // it's too close before deathmatch
        else if (this.currentPhase != Phase.USED && this.currentPhase != Phase.ACTIVE && textMessage.contains("The Blitz Star has been disabled!")) {
            this.currentPhase = Phase.FORFEIT;
        }
    }

    public String getRenderingString() {
        if (currentPhase == null)
            return "";
        switch (this.currentPhase) {
            case NOT_RELEASED:
                // display nothing
                return "Not released";
            case HIDDEN:
                // it's hidden
                return TextFormatting.YELLOW + "Hidden";
            case FOUND:
                // tell the player who had it.
                return TextFormatting.LIGHT_PURPLE + (this.owner.isEmpty() ? "Found" : this.owner);
            case ACTIVE:
                return TextFormatting.RED + this.owner + " -> " + this.activatedBlitz;
            case FORFEIT:
            case USED:
                // it's gone
                return TextFormatting.GREEN + "Gone";
        }
        return "";
    }

    @Override
    public boolean showElement() {
        return doesMatchForGame() && enabled;
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }

    private enum Phase {NOT_RELEASED, HIDDEN, FOUND, ACTIVE, USED, FORFEIT}
}
