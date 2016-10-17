package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider;
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class WallsKillCounterModularGuiProvider extends SimpleHudPixelModularGuiProvider implements McColorHelper {
    @ConfigPropertyBoolean(catagory = "the walls", id = "wallsKillCounter", comment = "The Walls Kill Tracker", def = true)
    public static boolean enabled = false;
    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.THE_WALLS);
    }

    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: " + EnumChatFormatting.RED;
    private static final String ASSISTS_DISPLAY = EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Assists: " + EnumChatFormatting.DARK_GRAY;

    public enum KillType {Normal, Assists}

    ;

    private KillType trackedType = KillType.Normal;
    private int kills;


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
                // I hope killed by includes all possible deaths
                // known common messages:
                // was killed by
                // was shot and killed by
                if (textMessage.contains("killed by " + FMLClientHandler.instance().getClient().getSession().getUsername())) {
                    this.kills++;
                }
                break;
            case Assists:
                // Assists are displayed using the coin message
                if (textMessage.contains("ASSIST") && textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
                    this.kills++;
                }
                break;
        }
    }

    public String getRenderingString() {
        switch (trackedType) {
            case Normal:
                return KILL_DISPLAY + this.kills;
            case Assists:
                return ASSISTS_DISPLAY + this.kills;
        }
        return "";
    }

    @Override
    public boolean showElement() {
        return doesMatchForGame() && !GameDetector.isLobby() && enabled;
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public String getAfterstats() {
        return YELLOW + "You got a total of " + GREEN + kills + YELLOW + " kills.";
    }
}
