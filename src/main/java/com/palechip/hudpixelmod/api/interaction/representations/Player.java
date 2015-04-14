package com.palechip.hudpixelmod.api.interaction.representations;

import com.google.gson.JsonObject;

/**
 * Processes PlayerReply's
 */
public class Player {
    // this contains everything
    protected JsonObject player;
    
    
    // the following information isn't complete. If a value is missing, check the player request.
    
    // arrays
    protected JsonObject stats;
    
    // simple properties
    protected String uuid;
    protected String playername;
    protected String displayname;
    protected String packageRank;
    
    protected int vanityTokens;
    protected int tournamentTokens;
    protected int networkLevel;
    protected int networkExp;
    protected int karma;
    
    protected int tipsSent;
    protected int thanksSent;
    protected int thanksReceived;
    protected long timePlaying;
    protected long lastLogin;
    protected long firstLogin;
    
    public JsonObject getStats() {
        return stats;
    }
}
