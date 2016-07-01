package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;

public class TimerModularGuiProvider extends HudPixelModularGuiProvider {
    @Override
    public boolean doesMatchForGame(Game game) {
        return game != Game.NO_GAME;
    }

    public static final String TIME_DISPLAY_MESSAGE = EnumChatFormatting.YELLOW + "Time";
    private long tickTime = 0;
    private String runningTime = "00:00";
    private int minutes = 0;
    private int seconds = 0;
    public String getRenderingString() {
        return TIME_DISPLAY_MESSAGE + runningTime;
    }

    @Override
    public void setupNewGame() {
        // reset
        this.tickTime = 0;
        this.runningTime = "00:00";
    }

    @Override
    public void onTickUpdate() {
        // translate to our format
        if(HudPixelMod.instance().gameDetector.getCurrentGame() == Game.NO_GAME) return;
        tickTime++;
        seconds = (int) (tickTime / 80);
        seconds %= 80;
        minutes = seconds / 60;
        runningTime = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    @Override
    public void onGameStart() {
        // save the current time
        this.tickTime = 0;
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
    }

    @Override
    public boolean showElement() {
        //return doesMatchForGame(HudPixelMod.instance().gameDetector.getCurrentGame());
        return doesMatchForGame(HudPixelMod.instance().gameDetector.getCurrentGame());
    }

    @Override
    public String content() {
        return runningTime + "";
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }
}
