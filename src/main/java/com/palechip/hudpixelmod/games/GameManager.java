package com.palechip.hudpixelmod.games;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;
import com.palechip.hudpixelmod.util.GameType;

/**
 * Manages all Games and can load them from a json file. Get your game here.
 * @author palechip
 */
public class GameManager {
    // hold a static reference to the used GameManager, this creates an empty one
    private static GameManager theGameManager = new GameManager();


    private boolean isEmpty;

    private ArrayList<GameConfiguration> configurations;
    private ComponentsManager componentsManager;
    /**
     * Creates an empty instance. Better than null.
     */
    private GameManager() {
        this.isEmpty = true;
    }

    /***
     * Creates a GameManager.
     * @param gameConfigFile a json file containing GameConfigurations of all games.
     */
    public GameManager(JsonArray gameConfig, JsonArray componentConfig) {
        HudPixelMod logger = HudPixelMod.instance();
        try {
            logger.logInfo("Trying to read a game configuration file...");

            // serialize the configurations
            Gson gson = new Gson();
            configurations = new ArrayList<GameConfiguration>();

            // go through all configurations provided by the file
            for(JsonElement config : gameConfig) {
                // serialize the element using gson and add it to the configurations.
                configurations.add(gson.fromJson(config, GameConfiguration.class));
                // log the result for debugging
                logger.logDebug("Game parsed: " + configurations.get(configurations.size() - 1));
            }

            // setup the Component Manager
            componentsManager = new ComponentsManager(this.configurations, componentConfig);

            // set the gamemanager, so that it will be used
            theGameManager = this;

            logger.logInfo("Finished reading the game configuration.");
        } catch (Exception e) {
            logger.logError("BIG PROBLEM! Failed to read the game configurations! The mod isn't going to work properly!");
            e.printStackTrace();
        }
    }

    /**
     * Create a game from it's configuration
     * @return A Game instance or NO_GAME
     */
    public Game createGame(int modID) {
        // get the correct configuration
        GameConfiguration config = this.getGameConfiguration(modID);
        // check if it's a valid configuration
        if(config == GameConfiguration.NULL_GAME) {
            // not valid, return NO_GAME
            return Game.NO_GAME;
        } else {
            // create the game
            return (new Game(config, this));
        }
    }

    /**
     * Create a game from it's configuration
     * @return A Game instance or NO_GAME
     */
    public Game createGame(GameType type) {
        return this.createGame(type.getModID());
    }

    /**
     * Get a game configuration by ModID.
     * @return the game configuration or GameConfiguration.NULL_GAME
     */
    public GameConfiguration getGameConfiguration(int modID) {
        if(!this.isEmpty) {
            // go through all configurations and pick the right one
            for(GameConfiguration config : this.configurations) {
                if(config.getModID() == modID) {
                    HudPixelMod.instance().logDebug("Found configuration for modID " + modID);
                    return config;
                }
            }
        }
        // return NULL_GAME if no configuration was found.
        return GameConfiguration.NULL_GAME;
    }

    /**
     * Get a game configuration by the GameType.
     * @return the game configuration or GameConfiguration.NULL_GAME
     */
    public GameConfiguration getGameConfiguration(GameType type) {
        return this.getGameConfiguration(type.getModID());
    }

    /**
     * Get all loaded configurations.
     */
    public ArrayList<GameConfiguration> getConfigurations() {
        return configurations;
    }

    /**
     * Get the component manager associated with the Game Manager.
     */
    public ComponentsManager getComponentsManager() {
        return this.componentsManager;
    }

    /**
     * Get the one and only GameManager. It may have empty values but it will never be null.
     */
    public static GameManager getGameManager() {
        return theGameManager;
    }
}
