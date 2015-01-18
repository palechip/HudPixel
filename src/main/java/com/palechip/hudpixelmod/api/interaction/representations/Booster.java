package com.palechip.hudpixelmod.api.interaction.representations;

import com.palechip.hudpixelmod.util.UuidHelper;

import net.hypixel.api.util.GameType;

public class Booster {
    private int amount;
    private long dateActivated;
    private int gameType;
    private long length;
    private long originalLength;
    private String purchaser;
    private String purchaserUuid;
    
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
        if(this.purchaser == null) {
            this.purchaser = UuidHelper.getUsernameFormUUID(purchaserUuid);
            
        }
        return purchaser;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Booster) {
            Booster b = (Booster)obj;
            return this.dateActivated == b.dateActivated && this.purchaser.equals(b.purchaser) && this.gameType == b.gameType;
        }
        return super.equals(obj);
    }
}
