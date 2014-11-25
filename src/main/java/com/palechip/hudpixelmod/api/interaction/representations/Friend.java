package com.palechip.hudpixelmod.api.interaction.representations;

public class Friend {
    private String _id;
    private String receiver;
    private String sender;
    private long started;
    
    private String friendName;
    
    public long getStartingTime() {
        return this.started;
    }
    
    public String getFriendName() {
        return this.friendName;
    }
    
    public boolean didFriendSendRequest() {
        return this.sender.equals(this.friendName);
    }
    
    public String getId() {
        return this._id;
    }
    /**
     * Used because there is no way to see which one is the friend and which is the player
     * @param player The player for which the request was sent
     */
    public void setPlayer(String player) {
        if(this.sender.equals(player)) {
            this.friendName = this.receiver;
        } else {
            this.friendName = this.sender;
        }
    }
}
