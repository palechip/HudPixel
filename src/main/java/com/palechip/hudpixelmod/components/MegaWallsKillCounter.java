package com.palechip.hudpixelmod.components;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.EnumChatFormatting;

public class MegaWallsKillCounter implements IComponent {
    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: ";
    private static final String FINAL_KILL_DISPLAY = EnumChatFormatting.BLUE + "Final Kills: ";
    public static enum KillType {Normal, Final};
    
    private KillType trackedType;
    private int kills;

    public MegaWallsKillCounter(KillType type) {
        this.trackedType = type;
    }

    @Override
    public void setupNewGame() {
        this.kills = 0;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        switch (this.trackedType) {
        case Normal:
            if(textMessage.contains("was killed by " + FMLClientHandler.instance().getClient().getSession().getUsername()) && !textMessage.contains("FINAL KILL")) {
                this.kills++;
            }
            break;
        case Final:
            if(textMessage.contains("was killed by " + FMLClientHandler.instance().getClient().getSession().getUsername()) && textMessage.contains("FINAL KILL")) {
                this.kills++;
            }
            break;
        }
    }

    @Override
    public String getRenderingString() {
        switch (this.trackedType) {
        case Normal:
            return KILL_DISPLAY + this.kills;
        case Final:
            return FINAL_KILL_DISPLAY + this.kills;
        }
        return "";
    }

}
