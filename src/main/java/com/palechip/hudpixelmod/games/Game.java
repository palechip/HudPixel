package com.palechip.hudpixelmod.games;

import java.util.ArrayList;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;
import com.palechip.hudpixelmod.games.arcade.BlockingDead;
import com.palechip.hudpixelmod.games.arcade.BountyHunters;
import com.palechip.hudpixelmod.games.arcade.CreeperAttack;
import com.palechip.hudpixelmod.games.arcade.DragonWars;
import com.palechip.hudpixelmod.games.arcade.EnderSpleef;
import com.palechip.hudpixelmod.games.arcade.FarmHunt;
import com.palechip.hudpixelmod.games.arcade.PartyGames1;
import com.palechip.hudpixelmod.games.arcade.PartyGames2;
import com.palechip.hudpixelmod.games.arcade.ThrowOut;
import com.palechip.hudpixelmod.games.tnt.BowSpleef;
import com.palechip.hudpixelmod.games.tnt.Run;
import com.palechip.hudpixelmod.games.tnt.Tag;
import com.palechip.hudpixelmod.games.tnt.Wizards;

public abstract class Game {
    // an array of all game classes
    private static ArrayList<Game> games;

    static {
        Game.loadGames();
    }

    /**
     * Initialize all Games. All config settings for games are processed with this.
     */
    public static void loadGames() {
        games = new ArrayList<Game>();

        games.add(new Quakecraft());
        games.add(new MegaWalls());
        games.add(new TheWalls());
        games.add(new VampireZ());
        games.add(new Paintball());
        games.add(new Arena());
        games.add(new Blitz());
        games.add(new Tag());
        games.add(new Run());
        games.add(new BowSpleef());
        games.add(new Wizards());
        games.add(new BountyHunters());
        games.add(new CreeperAttack());
        games.add(new DragonWars());
        games.add(new EnderSpleef());
        games.add(new FarmHunt());
        games.add(new PartyGames1());
        games.add(new PartyGames2());
        games.add(new ThrowOut());
        games.add(new BlockingDead());
        //Add games here.
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<IComponent> components;

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
        this.renderStrings = new ArrayList<String>();
        this.components = new ArrayList<IComponent>();

        CHAT_TAG = chatTag;
        BOSSBAR_NAME = bossbarName;
        START_MESSAGE = startMessage;
        END_MESSSSAGE = endMessage;
    }

    public  void setupNewGame() {
        this.renderStrings.clear();
        for(IComponent component : this.components) {
            component.setupNewGame();
            this.renderStrings.add(component.getRenderingString());
        }
    }

    protected void onGameStart() {
        for(IComponent component : this.components) {
            component.onGameStart();
        }
    }

    /**
     * Called when the game ends.
     */
    protected void onGameEnd() {
        for(IComponent component : this.components) {
            component.onGameEnd();
        }
        // display the results
        HudPixelMod.instance().displayResults(this.getRenderStrings());
    }

    public void onTickUpdate() {
        for(IComponent component : this.components) {
            component.onTickUpdate();
        }
    }

    // this is called even if the game hasn't started
    public void updateRenderStrings() {
        for(int i = 0; i < components.size(); i++) {
            this.renderStrings.set(i, this.components.get(i).getRenderingString());
        }
    }

    public void onChatMessage(String textMessage, String formattedMessage) {
        for(IComponent component : this.components) {
            component.onChatMessage(textMessage, formattedMessage);
        }
    }

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
