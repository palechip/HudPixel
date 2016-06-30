package com.palechip.hudpixelmod.modulargui;

import com.palechip.hudpixelmod.games.Game;
import eladkay.modulargui.lib.IModularGuiProvider;

public abstract class HudPixelModularGuiProvider implements IModularGuiProvider {

    public abstract boolean doesMatchForGame(Game game);

    /**
     * This is called when the mod has detected that the player joined a game of this type.
     * It should reset the rendered strings to the default ones.
     */
    public abstract void setupNewGame();

    /**
     * Called when the game starts.
     */
    public abstract void onGameStart();

    /**
     * Called when the game ends.
     */
    public abstract void onGameEnd();

    /**
     * If the game is running, it'll receive ticks to update the rendered strings.
     */
    public abstract void onTickUpdate();

    /**
     * If the game is running, it'll receive the chat messages which the client receives.
     */
    public abstract void onChatMessage(String textMessage, String formattedMessage);

}
