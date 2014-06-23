package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Quakecraft extends Game {

    private static final int COIN_DISPLAY_INDEX = 0;

    CoinCounterComponent coinCounter;

    protected Quakecraft() {
        super("Quake", "", "The game is starting! Right-click to shoot with your Railgun to instakill players!", "You earned a total of");
        this.coinCounter = new CoinCounterComponent();
    }

    @Override
    public void setupNewGame() {
        this.coinCounter.setupNewGame();
        
        this.renderStrings.clear();
        this.renderStrings.add(this.coinCounter.getRenderingString());
    }

    @Override
    protected void onGameStart() {
    }

    @Override
    protected void onGameEnd() {
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        this.coinCounter.onChatMessage(textMessage, formattedMessage);
        if(!this.coinCounter.getRenderingString().equals(this.renderStrings.get(COIN_DISPLAY_INDEX))) {
            this.renderStrings.set(COIN_DISPLAY_INDEX, this.coinCounter.getRenderingString());
        }
    }

}
