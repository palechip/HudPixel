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
package com.palechip.hudpixelmod.stats;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.PlayerResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Player;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

public abstract class StatsDisplayer implements PlayerResponseCallback{

    protected String playerName;
    protected JsonObject statistics;
    protected boolean shouldPrint;

    protected StatsDisplayer(String playerName) {
        this.playerName = playerName;
        if(HudPixelConfig.useAPI && HudPixelConfig.enableAfterStats) {
            Queue.getInstance().getPlayer(this, playerName);
        }
    }

    /**
     * Tells the stats displayer to show the stats in chat.
     * The will be printed once they are ready.
     */
    public void display() {
        if(statistics != null) {
            // here we go
            try {
                this.displayStatsInChat();
            } catch(Exception e) {
                HudPixelMod.instance().logError("Failed to display stats.");
                e.printStackTrace();
            }
        } else {
            // note that we should print the stats once they are ready
            this.shouldPrint = true;
        }
    }
    
    public void onPlayerResponse(Player player) {
        if(player != null){
            if(player.getStats() != null) {
                this.statistics = player.getStats();
                if (this.shouldPrint) {
                    this.display();
                }
            } else {
                this.failed();
                return;
            }
        } else {
            this.failed();
            return;
        }
    }
    
    private void failed() {
        new ChatMessageComposer("Failed to load stats for: " + this.playerName + "!", EnumChatFormatting.RED).send();
    }
    
    /**
     * Prints relevant statistics of a game to the chat.
     * Gets called when statistics are not null.
     */
    protected abstract void displayStatsInChat();
}
