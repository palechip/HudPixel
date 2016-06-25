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
import net.minecraftforge.fml.client.FMLClientHandler;

public class KillstreakTracker implements IComponent {
    private int currentKillstreak;
    private int greatestKillstreak;

    private boolean showGreatest;

    private final String CURRENT_KILLSTREAK_DISPLAY_TEXT = EnumChatFormatting.DARK_PURPLE + "Killstreak: ";
    private final String GREATEST_KILLSTREAK_DISPLAY_TEXT = EnumChatFormatting.LIGHT_PURPLE + "Best Killstreak: ";

    @Override
    public void setupNewGame() {
        //reset
        this.currentKillstreak = 0;
        this.greatestKillstreak = 0;
        this.showGreatest = false;
    }

    @Override
    public void onGameEnd() {
        this.showGreatest = true;
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        String username = FMLClientHandler.instance().getClient().func_110432_I().func_111285_a();
        // Quake
        if(textMessage.contains(username + " gibbed ")) {
            this.addKill();
        } else if(textMessage.contains(" gibbed " + username)) {
            this.resetKillstreak();
        }
        // TNT Wizards
        else if(textMessage.contains("You killed ")) {
            this.addKill();
        } else if(textMessage.contains("You were killed by ")) {
            this.resetKillstreak();
        }
        // Dragon Wars
        else if(textMessage.contains(username + " killed ")) {
            this.addKill();
        } else if(textMessage.contains(" killed " + username)) {
            this.resetKillstreak();
        }
        // Bounty Hunters
        else if(textMessage.contains(" was killed by " + username)) {
            this.addKill();
        } else if(textMessage.contains(username + " was killed ")) {
            this.resetKillstreak();
        }
        // Throw Out
        else if(textMessage.contains(" was punched into the void by " + username)) {
            this.addKill();
        } else if(textMessage.contains(username + " was punched into the void by ") || textMessage.contains(username + " became one with the void!")) {
            this.resetKillstreak();
        }
    }

    @Override
    public String getRenderingString() {
        if(this.showGreatest) {
            return GREATEST_KILLSTREAK_DISPLAY_TEXT + this.greatestKillstreak;
        } else {
            return CURRENT_KILLSTREAK_DISPLAY_TEXT + this.currentKillstreak;
        }
    }

    private void addKill() {
        this.currentKillstreak++;
        if(this.currentKillstreak > this.greatestKillstreak) {
            this.greatestKillstreak = this.currentKillstreak;
        }
    }

    private void resetKillstreak() {
        this.currentKillstreak = 0;
    }

    @Override
    public void onTickUpdate() { }

    @Override
    public void onGameStart() { }

    @Override
    public String getConfigName() {
        return "KillstreakTracker";
    }

    @Override
    public String getConfigComment() {
        return "Enable/Disable tracking how many kills you can get within one live for %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }
}
