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
import eladkay.hudpixel.modulargui.SimpleHudPixelModularGuiProvider;
import eladkay.hudpixel.util.GameType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.unaussprechlich.hudpixelextended.util.McColorHelper;

import java.util.ArrayList;

public class TkrTimerModularGuiProvider extends SimpleHudPixelModularGuiProvider implements McColorHelper {
    public static final String LAP_COMPLETION_MESSAGE_REGEX = "(Lap \\d Completed!).*";
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "kartRacersAccurateTimeDisplay", comment = "The TKR Time Tracker", def = true)
    public static boolean enabled = false;
    private static long startDelay = 0L;
    private int lap = 0;
    private boolean running = false;
    private long startingTime = 0;
    private String runningTime = "00:00";

    private String officialTime = "";

    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.TURBO_KART_RACERS);
    }

    @Override
    public void setupNewGame() {
    }

    @Override
    public void onGameStart() {
        // start the general timer and the first lap timer
        if (this.lap == 0 || this.lap == 1) {
            // add the start delay. Setting the start into the future if we know the start delay
            this.startingTime = System.currentTimeMillis() + startDelay;
            this.running = true;
        }
    }

    @Override
    public void onGameEnd() {
        this.running = false;
    }

    @Override
    public void onTickUpdate() {
        if (this.running) {
            // update the time
            long timeDifference = System.currentTimeMillis() - this.startingTime;
            long timeDifferenceSeconds = timeDifference / 1000;
            long timeDifferenceMinutes = timeDifferenceSeconds / 60;

            // translate to our format
            this.runningTime = (timeDifference < 0 ? "-" : "") + ((Math.abs(timeDifferenceMinutes % 60) < 10) ? "0" : "") + Math.abs(timeDifferenceMinutes % 60) + ":" + ((Math.abs(timeDifferenceSeconds) % 60 < 10) ? "0" : "") + Math.abs(timeDifferenceSeconds % 60);
        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // isHypixelNetwork if the message is relevant
        if (textMessage.matches(LAP_COMPLETION_MESSAGE_REGEX)) {
            try {
                // the lap number is the 5th character. It needs to be cast to String first because otherwise we get the wrong value
                int lapNo = Integer.valueOf(String.valueOf(textMessage.charAt(4)));

                // isHypixelNetwork if the listened lap was completed
                if (this.lap == lapNo) {
                    // extract the start message
                    this.officialTime = textMessage.substring(textMessage.indexOf('(') + 1, textMessage.indexOf(')'));
                    // stop the timer
                    this.running = false;
                }

                // start the next timer
                if (this.lap - 1 == lapNo) {
                    this.running = true;
                    // there is no delay here
                    this.startingTime = System.currentTimeMillis();
                }

                // accuracy isHypixelNetwork and correction for the main timer after the first lap
                if (lapNo == 1 && this.lap == 0) {
                    // save the current time
                    long currentTime = System.currentTimeMillis();
                    // save the measured time
                    long measuredTime = currentTime - this.startingTime;

                    ArrayList<Integer> officialTime = new ArrayList<Integer>(2);
                    // convert the officialTime
                    for (String s : textMessage.substring(textMessage.indexOf('(') + 1, textMessage.indexOf(')')).split(":")) {
                        officialTime.add(Integer.valueOf(s));
                    }
                    // convert everything into milliseconds
                    long officialTimeMilliSeconds = officialTime.get(0) * 60 * 1000 + officialTime.get(1) * 1000;

                    // correct the main timer
                    this.startingTime = currentTime - officialTimeMilliSeconds;

                    // the start delay is the difference between our (greater) measured time and the official time
                    startDelay = startDelay + (measuredTime - officialTimeMilliSeconds);
                }
            } catch (Exception e) {
                e.printStackTrace();
                HudPixelMod.Companion.instance().logWarn("Failed to parse the lap completion message. Ignoring");
            }
        }
        // stop the general timer when finishing
        if (textMessage.startsWith(FMLClientHandler.instance().getClient().getSession().getUsername() + " has finished the race at position ") && this.lap == 0) {
            this.running = false;
        }
    }

    public String getRenderingString() {
        // for the general timer
        if (this.lap == 0) {
            // show the running time
            return TimerModularGuiProvider.TIME_DISPLAY_MESSAGE + this.runningTime;
        } else {
            if (this.running) {
                // show the result if the timer is running
                return TextFormatting.YELLOW + "Lap " + this.lap + ": " + this.runningTime;
            } else {
                // return the official time if it isn't running. This may also be empty if it hasn't started yet
                return this.officialTime.isEmpty() ? "" : TextFormatting.YELLOW + "Lap " + this.lap + ": " + this.officialTime;
            }
        }
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
        return YELLOW + "You played a total of " + GREEN + lap + YELLOW + " laps.";
    }
}
