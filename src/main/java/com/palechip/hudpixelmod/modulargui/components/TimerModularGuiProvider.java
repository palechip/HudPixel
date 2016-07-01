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
    private long gameStartedTime;
    private boolean gameStarted;
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
        if(gameStarted){
            long timeBuff = System.currentTimeMillis() - gameStartedTime;
            String sMin;
            long min = (timeBuff/1000/60);
            if(min < 10) sMin = "0" + min;
            else         sMin = ""  + min;
            String sSec;
            long sec = (timeBuff/1000) - (min*60);
            if(sec < 10) sSec = "0" + sec;
            else         sSec = ""  + sec;
            runningTime = sMin + ":" + sSec;
        }
    }

    @Override
    public void onGameStart() {
        gameStarted = true;
        gameStartedTime = System.currentTimeMillis();
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
