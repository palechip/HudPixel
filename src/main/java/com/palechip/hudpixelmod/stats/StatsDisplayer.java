package com.palechip.hudpixelmod.stats;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;

import com.google.gson.Gson;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.PlayerResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Player;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

public abstract class StatsDisplayer implements PlayerResponseCallback{

    protected String playerName;
    protected Map<String, Object> statistics;
    protected boolean shouldPrint;

    protected StatsDisplayer(String playerName) {
        this.playerName = playerName;
        Queue.getInstance().getPlayer(this, playerName);
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
                this.statistics = (Map)Queue.getInstance().getGson().fromJson(player.getStats(), Map.class);
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
