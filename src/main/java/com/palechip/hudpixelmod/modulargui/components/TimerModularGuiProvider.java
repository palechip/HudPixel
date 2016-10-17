package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.EnumChatFormatting;

public class TimerModularGuiProvider extends HudPixelModularGuiProvider {
    @ConfigPropertyBoolean(catagory = "general", id = "timer", comment = "The Game Timer", def = true)
    public static boolean enabled = false;
    @Override
    public boolean doesMatchForGame() {
        return GameDetector.getCurrentGameType() != GameType.UNKNOWN;
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
        if (gameStarted && !GameDetector.isLobby()) {
            long timeBuff = System.currentTimeMillis() - gameStartedTime;
            String sMin;
            long min = (timeBuff / 1000 / 60);
            if (min < 10) sMin = "0" + min;
            else sMin = "" + min;
            String sSec;
            long sec = (timeBuff / 1000) - (min * 60);
            if (sec < 10) sSec = "0" + sec;
            else sSec = "" + sec;
            runningTime = sMin + ":" + sSec;
        } else {
            setupNewGame();
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
        return doesMatchForGame() && !GameDetector.isLobby() && enabled;
    }

    @Override
    public String content() {
        return runningTime + "";
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return YELLOW + "Time passed: " + GREEN + runningTime;
    }
}
