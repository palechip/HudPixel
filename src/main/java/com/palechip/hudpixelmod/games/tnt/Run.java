package com.palechip.hudpixelmod.games.tnt;

import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Run extends Game {
    private static final int TIME_DISPLAY_INDEX = 0;

    TimerComponent timer;

    public Run() {
        super("TNT Run", "", "Go!", "You earned a total of");
        this.timer = new TimerComponent();
    }

    @Override
    public void setupNewGame() {
        this.timer.setupNewGame();

        this.renderStrings.clear();
        this.renderStrings.add(this.timer.getRenderingString());

    }

    @Override
    protected void onGameStart() {
        this.timer.onStartGame();
    }

    @Override
    protected void onGameEnd() {
    }

    @Override
    public void onTickUpdate() {
        this.timer.onTickUpdate();
        this.renderStrings.set(TIME_DISPLAY_INDEX, this.timer.getRenderingString());
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
    }
}
