package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class MWKillCounterModularGuiProvider extends SimpleHudPixelModularGuiProvider implements McColorHelper {
    @Override
    public boolean doesMatchForGame(Game game) {
        return game.getConfiguration().getDatabaseName().equals("Walls3");
    }

    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: " + EnumChatFormatting.RED;
    private static final String FINAL_KILL_DISPLAY = EnumChatFormatting.BLUE + "Final Kills: " + EnumChatFormatting.RED;
    private static final String ASSISTS_DISPLAY = EnumChatFormatting.AQUA +  "" + EnumChatFormatting.ITALIC +"Assists: " + EnumChatFormatting.DARK_GRAY;
    private static final String FINAL_ASSISTS_DISPLAY = EnumChatFormatting.BLUE +  "" + EnumChatFormatting.ITALIC +"Final Assists: " + EnumChatFormatting.DARK_GRAY;
    private static final String WITHER_COINS_DISPLAY = EnumChatFormatting.GOLD + "Wither Coins: ";
    public static enum KillType {Normal, Final, Assists, Final_Assists, Wither_Coins};

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
        // coin message?, not from tipping
        if(textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins") && !textMessage.toLowerCase().contains("for being generous :)")) {
            switch (this.trackedType) {
                case Normal:
                    // exclude wither rushing reward
                    if(!textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL") && !textMessage.contains("Wither Damage")) {
                        this.kills++;
                    }
                    // some ninja detection for kills over 18
                    if(this.kills >= 18 && textMessage.contains("was killed by " + FMLClientHandler.instance().getClient().getSession().getUsername())) {
                        this.kills++;
                    }
                    break;
                case Final:
                    if(!textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                        this.kills++;
                    }
                    break;
                case Assists:
                    if(textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL")) {
                        this.kills++;
                    }
                    break;
                case Final_Assists:
                    if(textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
                        this.kills++;
                    }
                    break;
                case Wither_Coins:
                    if(textMessage.contains("Wither Damage")) {
                        this.kills += CoinCounterModularGuiProvider.getCoinsFromMessage(textMessage);
                    }
                    break;
            }
        }
    }

    public String getRenderingString() {
        switch (this.trackedType) {
            case Normal:
                return KILL_DISPLAY + this.kills;
            case Final:
                return FINAL_KILL_DISPLAY + this.kills;
            case Assists:
                return ASSISTS_DISPLAY + this.kills;
            case Final_Assists:
                return FINAL_ASSISTS_DISPLAY + this.kills;
            case Wither_Coins:
                return this.kills > 0 ? WITHER_COINS_DISPLAY + this.kills : "";
        }
        return "";
    }

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

    @Override
    public String getAfterstats() {
        return YELLOW + "You got a total of " + GREEN + kills + YELLOW + " Kills.";
    }
}
