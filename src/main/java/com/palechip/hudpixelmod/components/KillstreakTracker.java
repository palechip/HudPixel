package com.palechip.hudpixelmod.components;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.EnumChatFormatting;

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
        String username = FMLClientHandler.instance().getClient().getSession().getUsername();
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
}
