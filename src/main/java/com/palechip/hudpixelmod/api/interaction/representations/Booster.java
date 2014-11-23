package com.palechip.hudpixelmod.api.interaction.representations;

import net.hypixel.api.util.GameType;

public class Booster {
    private int amount;
    private long dateActivated;
    private int gameType;
    private long length;
    private long originalLength;
    private String purchaser;
    
    public Booster(){}
    
    public int getCoinAmount() {
        return amount;
    }
    
    public long getActivationDateAndTime() {
        return dateActivated;
    }
    
    public GameType getGame() {
        return GameType.fromId(gameType);
    }
    
    public long getRemainingTime() {
        return length;
    }
    
    public long getTotalLength() {
        return originalLength;
    }
    
    public String getOwner() {
        return purchaser;
    }
}
