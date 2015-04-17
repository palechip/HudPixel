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
