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

import java.util.HashMap;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class PaintballKillstreakTracker implements IComponent {
    private static final String COOLDOWN_SIGN = EnumChatFormatting.RED + "\u2717"; // fancy x
    private static final String ACTIVE_SIGN = EnumChatFormatting.GREEN + "\u2713"; // check mark
    
    private static HashMap<String, PaintballKillstreakTracker> cooldownDependantKillstreaks = new HashMap<String, PaintballKillstreakTracker>();
    private static HashMap<String, Long> durationStorage = new HashMap<String, Long>();

    private String renderedString;
    private String listenedKillstreak;
    private boolean isTimed;
    private boolean isActive;
    private long startTime;
    private long duration;
    private boolean hasCooldown;
    private boolean isCoolingDown;

    private String cooldownDependantKillstreak;

    /**
     * Setup a Timer for a specific killstreak
     * @param killstreak The listened killsteak
     * @param isTimed True if the killstreak has a duration, otherwise it'll enter cooldown state upon activation
     * @param hasCooldown True if there is a cooldown for the usage of the killstreak
     * @param cooldownDependantKillstreak Specifys that this killstreak also enters cooldown when the given killstreak is cooling down
     */
    public PaintballKillstreakTracker(String killstreak, boolean isTimed, boolean hasCooldown, String cooldownDependantKillstreak) {
        this.listenedKillstreak = killstreak;
        this.isTimed = isTimed;
        // look if we have saved a duration value from past instances
        if(this.isTimed && durationStorage.containsKey(this.listenedKillstreak)) {
            // set it so we don't have to measure again
            this.duration = durationStorage.get(this.listenedKillstreak);
        }
        this.hasCooldown = hasCooldown;
        // add the string even when it is empty. It's better than null
        this.cooldownDependantKillstreak = cooldownDependantKillstreak;
        if (!cooldownDependantKillstreak.isEmpty()) {
            // if there is a killstreak which this depends on, there also is one which depends on this.
            // for this reason we add this to the list of cooldownDependantKillstreas
            cooldownDependantKillstreaks.put(this.listenedKillstreak, this);
        }
    }

    @Override
    public void setupNewGame() {
        this.renderedString = "";
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        this.isActive = false;
        this.isCoolingDown = false;
    }

    @Override
    public void onTickUpdate() {
        if(this.isActive) {
            // only if we already measured the length
            if(this.duration > 0) {
                long remainingTime = this.duration - (System.currentTimeMillis() - this.startTime);
                this.renderedString = this.getColorForTime(remainingTime / 1000) + remainingTime / 1000 + "s";
            } else {
                long timePast = System.currentTimeMillis() - this.startTime;
                this.renderedString = EnumChatFormatting.YELLOW + "" + timePast / 1000 + "s (m)";
            }
        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // test for starting
        if(textMessage.contains(FMLClientHandler.instance().getClient().func_110432_I().func_111285_a() + " activated " + this.listenedKillstreak)) {
            this.startTime = System.currentTimeMillis();
            if(this.isTimed) {
                this.isActive = true;
            } else if(this.hasCooldown) {
                this.isCoolingDown = true;
            }
        }
        // test for expiring
        if(textMessage.contains("Your " + this.listenedKillstreak + " has expired!")) {
            this.isActive = false;
            // update the duration
            this.duration = System.currentTimeMillis() - this.startTime;
            // and save it for future instances
            durationStorage.put(this.listenedKillstreak, this.duration);
            this.renderedString = "";
            if(this.hasCooldown) {
                // Start the cooldown
                this.isCoolingDown = true;
            }
        }
        // test if the cooldown is over
        if(textMessage.contains("You can now use " + this.listenedKillstreak + " again!")) {
            this.isCoolingDown = false;
        }
    }

    @Override
    public String getRenderingString() {
        if(this.isActive) {
            return ACTIVE_SIGN +  EnumChatFormatting.DARK_PURPLE + this.listenedKillstreak + ": " + renderedString;
        } else if(this.isCoolingDown || (!this.cooldownDependantKillstreak.isEmpty() && cooldownDependantKillstreaks.containsKey(this.cooldownDependantKillstreak) ? cooldownDependantKillstreaks.get(this.cooldownDependantKillstreak).isCoolingDown : false)) {
            // the listened killstreak will be red because the color from COOLDOWN_SIGN isn't reset
            return COOLDOWN_SIGN + " " + this.listenedKillstreak;
        } else  {
            return "";
        }
    }

    private String getColorForTime(long time) {
        if(time >= 10) {
            // green
            return String.valueOf(EnumChatFormatting.GREEN);
        } else if( time >= 5){
            // orange
            return String.valueOf(EnumChatFormatting.GOLD);
        } else {
            // red and bold
           return String.valueOf(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD);
        }
    }

    @Override
    public String getConfigName() {
        return "KillstreakTrackerDisplay";
    }

    @Override
    public String getConfigComment() {
        return "Show/Hide your active %game killstreaks (including timer) and the ones on cooldown.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }
}
