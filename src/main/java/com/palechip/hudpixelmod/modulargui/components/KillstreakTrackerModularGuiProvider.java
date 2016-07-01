package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class KillstreakTrackerModularGuiProvider extends SimpleHudPixelModularGuiProvider {
    @Override
    public boolean doesMatchForGame(Game game) {
        return game.getConfiguration().getDatabaseName().equals("TNTGames") || game.getConfiguration().getDatabaseName().equals("Quake") || game.getConfiguration().getDatabaseName().equals("Arcade");
    }

    private int currentKillstreak;
    private int greatestKillstreak;

    private boolean showGreatest;

    public static final String CURRENT_KILLSTREAK_DISPLAY_TEXT = EnumChatFormatting.DARK_PURPLE + "Killstreak: ";
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
        // Throw Out
        else if(textMessage.contains(" was punched into the void by " + username)) {
            this.addKill();
        } else if(textMessage.contains(username + " was punched into the void by ") || textMessage.contains(username + " became one with the void!")) {
            this.resetKillstreak();
        }
    }

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
    public boolean showElement() {
        return doesMatchForGame(HudPixelMod.instance().gameDetector.getCurrentGame());
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }
}
