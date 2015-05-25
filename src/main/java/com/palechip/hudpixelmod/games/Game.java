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

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;
import com.palechip.hudpixelmod.util.GameType;

public class Game {
    public static final Game NO_GAME = new Game();

    protected GameConfiguration configuration;
    protected ArrayList<IComponent> components;

    // the strings which the game wants to be rendered
    protected ArrayList<String> renderStrings;

    // is the player in a game of this type and the game has started
    protected boolean hasStarted;

    private Game() {
        this.renderStrings = new ArrayList<String>();
        this.components = new ArrayList<IComponent>();
        this.configuration = GameConfiguration.NULL_GAME;
    }
    
    public Game(GameConfiguration configuration, GameManager manager) {
        this();
        
        // save the configuration
        this.configuration = configuration;
        // get our components
        this.components = manager.getComponentsManager().getComponentInstances(configuration);
        
        HudPixelMod.instance().logDebug("Game created: " + this.toString());
    }

    @Override
    public String toString() {
        return "Game [configuration=" + configuration + ", components="
                + components + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Game) {
            return ((Game) obj).configuration.getModID() == this.configuration.getModID();
        } else if(obj instanceof GameType) {
            return ((GameType) obj).getModID() == this.configuration.getModID();
        } else {
            return false;
        }
    }

    public void setupNewGame() {
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
        HudPixelMod.instance().renderer.displayResults(this.getRenderStrings());
    }

    public void onTickUpdate() {
        for(IComponent component : this.components) {
            component.onTickUpdate();
        }
    }

    // this is called even if the game hasn't started
    public void updateRenderStrings() {
        this.renderStrings.clear();
        // add information about the game status for debug reasons
        if(HudPixelMod.IS_DEBUGGING) {
            renderStrings.add(this.configuration.getShortName() + " " + (this.hasStarted ? "started" : "not started"));
        }
        for(int i = 0; i < components.size(); i++) {
            // only add the string if it actually contains something
            // so if you set the display to bottom, it doesn't float
            if(!this.components.get(i).getRenderingString().isEmpty()) {
                this.renderStrings.add(this.components.get(i).getRenderingString());
            }
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

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public boolean hasGameStarted() {
        return hasStarted;
    }
}
