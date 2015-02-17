package com.palechip.hudpixelmod.api.interaction.representations;

import com.palechip.hudpixelmod.util.UuidHelper;

import net.hypixel.api.util.GameType;

public class Booster {
    public static final int TIPPING_COOLDOWN = 1800000; // = 30 min
    
    // these values are filled by the API
    // purchaser is only filled for old boosters
    private int amount;
    private long dateActivated;
    private int gameType;
    private long length;
    private long originalLength;
    private String purchaserUuid;
    
    // properties used by the mod
    private long tippingTime;
    private String owner;
    
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
        if(this.owner == null) {
            this.owner = UuidHelper.getUsernameFormUUID(purchaserUuid);
            
        }
        return owner;
    }
    
    /**
     * This saves the time when the booster was tipped.
     */
    public void tip() {
        this.tippingTime = System.currentTimeMillis();
    }
    
    public boolean canTip() {
        // this expression will be true if the booster is active and wasn't tipped or can be tipped again
        return (this.getRemainingTime() < this.getTotalLength() && (this.tippingTime == 0l || System.currentTimeMillis() > this.tippingTime + TIPPING_COOLDOWN ));
    }
    
    public void setTippingTime(long time) {
        this.tippingTime = time;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Booster) {
            Booster b = (Booster)obj;
            return this.dateActivated == b.dateActivated && this.owner.equals(b.owner) && this.gameType == b.gameType;
        }
        return super.equals(obj);
    }
}
