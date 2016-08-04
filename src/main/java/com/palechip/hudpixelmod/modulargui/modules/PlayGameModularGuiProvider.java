package com.palechip.hudpixelmod.modulargui.modules;

import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;

public class PlayGameModularGuiProvider extends HudPixelModularGuiProvider {

    public static String content = "";
    @Override
    public boolean doesMatchForGame(Game game) {
        return true;
    }

    @Override
    public void setupNewGame() {

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

    }

    @Override
    public boolean showElement() {
        return !content.equals("");
    }

    @Override
    public String content() {
        if(content.equals("solo_insane") || content.equals("solo_normal") || content.equals("team_insane") || content.equals("team_normal"))
            return EnumChatFormatting.AQUA + "Press P to play " + EnumChatFormatting.BLUE + "SkyWars";
        if(content.equals("blitz_solo_insane"))
            return EnumChatFormatting.AQUA + "Press P to play " + EnumChatFormatting.BLUE + "BSG";
        return content;
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return true;
    }

    @Override
    public String getAfterstats() {
        return null;
    }
}
