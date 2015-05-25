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
package com.palechip.hudpixelmod.api.interaction.representations;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.palechip.hudpixelmod.games.GameConfiguration;
import com.palechip.hudpixelmod.games.GameManager;

public class Session {
    // the player this session belongs to, isn't included in the response but still relevant
    private String sessionOwner;
    
    private String _id;
    private String gameType;
    private JsonArray players;
    private ArrayList<String> playersArray;
    private String server;
    
    private int gameID; // saves the ID the mod uses for the game and not the database ID.
    
    public String getID() {
        return this._id;
    }
    
    public int getGameID() {
        if(this.gameID == 0) {
            // go through all configurations
            for(GameConfiguration config : GameManager.getGameManager().getConfigurations()) {
                // if we find one with a matching database id
                if(config.getDatabaseName().equals(gameType)) {
                    // save it
                    this.gameID = config.getModID();
                }
            }
        }
        return this.gameID;
    }
    
    public String getServer() {
        return this.server;
    }
    
    public ArrayList<String> getPlayers() {
        // make sure we don't convert the array twice
        if(this.playersArray == null) {
            this.playersArray = new ArrayList<String>();
            for(JsonElement s : this.players) {
                if(s.isJsonPrimitive()) {
                    this.playersArray.add(s.getAsString());
                }
            }
        }
            return this.playersArray;
    }
    
    public String getSessionOwner() {
        return this.sessionOwner;
    }
    
    public void setSessionOwner(String owner) {
        this.sessionOwner = owner;
    }
}
