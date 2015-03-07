package com.palechip.hudpixelmod.stats;

import net.hypixel.api.util.GameType;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.util.ChatMessageComposer;

public class BlitzStatsDisplayer extends StatsDisplayer{

    public BlitzStatsDisplayer(String name) {
        super(name);
        new ChatMessageComposer("Getting " + name + "\'s stats!", EnumChatFormatting.GREEN).send();
    }

    @Override
    public void displayStatsInChat() {
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.YELLOW);
        new ChatMessageComposer(this.playerName + "\'s stats in " + GameType.SURVIVAL_GAMES, EnumChatFormatting.DARK_BLUE).send();
        new ChatMessageComposer("Kills: " , EnumChatFormatting.DARK_BLUE).appendMessage(new ChatMessageComposer(this.statistics.get("kills").toString(), EnumChatFormatting.RED)).appendMessage(new ChatMessageComposer(" Wins: ",EnumChatFormatting.DARK_BLUE)).appendMessage(new ChatMessageComposer(this.statistics.get("wins").toString(), EnumChatFormatting.RED)).send();
        ChatMessageComposer.printSeparationMessage(EnumChatFormatting.YELLOW);
    }
}
