package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;

public class SimpleTitleModularGuiProvider extends SimpleHudPixelModularGuiProvider {
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
        return true;
    }

    @Override
    public String content() {
        return EnumChatFormatting.AQUA + "HudPixel Reloaded " + HudPixelMod.DEFAULT_VERSION;
    }

    @Override
    public String getAfterstats() {
        return null;
    }
}
