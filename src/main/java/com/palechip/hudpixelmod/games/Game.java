package com.palechip.hudpixelmod.games;

import java.util.ArrayList;

import com.palechip.hudpixelmod.games.arcade.CreeperAttack;

public abstract class Game {
    // an array of all game classes
    private static ArrayList<Game> games;

    static {
        games = new ArrayList<Game>();

        games.add(new Quakecraft());
        games.add(new MegaWalls());
        games.add(new CreeperAttack());
        //Add games here.
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    // set of strings which are characteristic for the game
    protected String CHAT_TAG;
    protected String BOSSBAR_NAME;
    protected String START_MESSAGE;
    protected String END_MESSSSAGE;

    // the strings which the game wants to be rendered
    protected ArrayList<String> renderStrings;

    // is the player in a game of this type and the game has started
    protected boolean hasStarted;


    protected Game(String chatTag, String bossbarName, String startMessage, String endMessage) {
        CHAT_TAG = chatTag;
        BOSSBAR_NAME = bossbarName;
        START_MESSAGE = startMessage;
        END_MESSSSAGE = endMessage;
    }

    /**
     * This is called when the mod has detected that the player joined a game of this type.
     * It should reset the rendered strings to the default ones.
     */
    public abstract void setupNewGame();

    /**
     * Called when the game starts.
     */
    protected abstract void onGameStart();

    /**
     * Called when the game ends.
     */
    protected abstract void onGameEnd();

    /**
     * If the game is running, it'll receive ticks to update the rendered strings.
     */
    public abstract void onTickUpdate();

    /**
     * If the game is running, it'll receive the chat messages which the client receives.
     */
    public abstract void onChatMessage(String textMessage, String formattedMessage);

    /**
     * Start the game. Calls onGameStart().
     */
    public void startGame() {
        if(!this.hasStarted) {
            this.hasStarted = true;
            this.onGameStart();
        }
    }

    /**
     * End the game. Calls onGameEnd().
     */
    public void endGame() {
        if(this.hasStarted) {
            this.hasStarted = false;
            this.onGameEnd();
        }
    }

    /*
     * Simple getting methods.
     */
    public ArrayList<String> getRenderStrings() {
        return renderStrings;
    }

    public String getChatTag() {
        return CHAT_TAG;
    }

    public String getBossbarName() {
        return BOSSBAR_NAME;
    }

    public String getStartMessage() {
        return START_MESSAGE;
    }

    public String getEndMessage() {
        return END_MESSSSAGE;
    }

    public boolean hasGameStarted() {
        return hasStarted;
    }
}
