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
