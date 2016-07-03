/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.games;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
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
    public GameManager(JsonArray gameConfig) {
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
                logger.logInfo("Game parsed: " + configurations.get(configurations.size() - 1));
            }


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
     * Get the one and only GameManager. It may have empty values but it will never be null.
     */
    public static GameManager getGameManager() {
        return theGameManager;
    }
}
