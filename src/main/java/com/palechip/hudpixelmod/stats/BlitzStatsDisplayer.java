package com.palechip.hudpixelmod.stats;

import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.util.ChatMessageComposer;
import com.palechip.hudpixelmod.util.GameType;

public class BlitzStatsDisplayer extends StatsDisplayer{

    public BlitzStatsDisplayer(String name) {
        super(name);
        if(HudPixelConfig.useAPI && HudPixelConfig.enableAfterStats) {
            new ChatMessageComposer("Getting " + name + "\'s stats!", EnumChatFormatting.GREEN).send();
        }
    }

    @Override
    public void displayStatsInChat() {
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.YELLOW);
        new ChatMessageComposer(this.playerName + "\'s stats in " + GameManager.getGameManager().getGameConfiguration(GameType.BLITZ).getOfficialName(), EnumChatFormatting.GREEN).send();
        new ChatMessageComposer("Kills: " , EnumChatFormatting.GREEN).appendMessage(new ChatMessageComposer(this.statistics.get("HungerGames").getAsJsonObject().get("kills").getAsString(), EnumChatFormatting.GOLD)).appendMessage(new ChatMessageComposer(" Wins: ",EnumChatFormatting.GREEN)).appendMessage(new ChatMessageComposer(this.statistics.get("HungerGames").getAsJsonObject().get("wins").getAsString(), EnumChatFormatting.GOLD)).send();
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.YELLOW);
    }
}
