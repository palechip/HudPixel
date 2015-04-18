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
package com.palechip.hudpixelmod.api.interaction;

import java.util.ArrayList;
import java.util.UUID;

import net.hypixel.api.HypixelAPI;

import com.google.gson.Gson;
import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.callbacks.ApiKeyLoadedCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.FriendResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.PlayerResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.SessionResponseCallback;

public class Queue implements ApiKeyLoadedCallback{
    private static Queue instance;
    private ApiKeyHandler keyHandler;
    public boolean apiEnabled = false;
    private HypixelAPI api;
    // even though the queue doesn't need it, it holds a gson instance which is used by QueueEntry
    private Gson gson;
    private ArrayList<QueueEntry> queue;
    private boolean isLocked;
    // used to limit the amount of requests, every request produces heat
    private int heat;
    // current values limit the queue to 1 request every 5 seconds on average
    private static final int HEAT_PER_REQUEST = 5;
    private static final int HEAT_MAXIMUM = 100;
    private static final int HEAT_COOLDOWN_PER_SECOND = 1;
    // keeps track of last second, so it can reduce the heat
    private long sec;
    // saves when the mod last asked the user for a key
    private long lastKeyNotice;
    private static final int MIN_TIME_BETWEEN_KEY_NOTICES = 60000; // = 1 min
    private static final int API_DISABLED_TIMEOUT = 15000;
    public Queue() {
        instance = this;
        this.queue = new ArrayList<QueueEntry>();
        // Instantiate the API first, so the key can't be loaded when the api is still null
        this.api = HypixelAPI.getInstance();
        this.gson = new Gson();
        this.keyHandler = new ApiKeyHandler(this);
    }

    public void onChatMessage(String textMessage) {
        this.keyHandler.onChatMessage(textMessage);
    }
    
    public void onClientTick() {
        // request the key if necessary
        if(HudPixelConfig.useAPI && !this.apiEnabled && !this.queue.isEmpty()) {
            // if the api is disabled, the requests will expire
            if(this.queue.get(0).getCreationTime() + API_DISABLED_TIMEOUT < System.currentTimeMillis()) {
                this.queue.get(0).cancel();
                this.queue.remove(0);
            }
            if(this.lastKeyNotice == 0 || System.currentTimeMillis() > this.lastKeyNotice + MIN_TIME_BETWEEN_KEY_NOTICES) {
                this.lastKeyNotice = System.currentTimeMillis();
                this.keyHandler.requestApiKey();
            }
        }
        
        // run the queue
        if(HudPixelConfig.useAPI && this.apiEnabled && !this.queue.isEmpty() && !this.isLocked && this.heat < HEAT_MAXIMUM + HEAT_PER_REQUEST) {
            QueueEntry entry = this.queue.get(0);
            entry.run();
            this.queue.remove(0);
            this.heat += HEAT_PER_REQUEST;
        }
        
        long currentSec = System.currentTimeMillis() / 1000;
        if(this.sec < currentSec) {
            this.heat -= HEAT_COOLDOWN_PER_SECOND * (currentSec - this.sec);
            this.sec = currentSec;
        }
    }

    @Override
    public void ApiKeyLoaded(boolean failed, String key) {
        if(failed) {
            this.apiEnabled = false;
        } else {
            this.api.setApiKey(UUID.fromString(key));
            this.apiEnabled = true;
        }
    }
    
    public HypixelAPI getAPI() {
        return this.api;
    }
    
    public Gson getGson() {
        return gson;
    }
    
    /**
     * Queues a Booster request.
     * @param callback
     */
    public void getBoosters(BoosterResponseCallback callback) {
        if(HudPixelConfig.useAPI) {
            this.queue.add(new QueueEntry(callback));
        } else {
            callback.onBoosterResponse(null);
        }
    }
    
    /**
     * Queues a Session request.
     * @param callback
     */
    public void getSession(SessionResponseCallback callback, String player) {
        if(HudPixelConfig.useAPI) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            callback.onSessionRespone(null);
        }
    }
    
    /**
     * Queues a Friends request.
     * @param callback
     */
    public void getFriends(FriendResponseCallback callback, String player) {
        if(HudPixelConfig.useAPI) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            callback.onFriendResponse(null);
        }
    }
    
    /**
     * Queues a Player request.
     * @param callback
     * @param player
     */
    public void getPlayer(PlayerResponseCallback callback, String player) {
        if(HudPixelConfig.useAPI) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            callback.onPlayerResponse(null);
        }
    }
    
    public void reportFailure(Throwable failCause, boolean secondTry) {
        HudPixelMod logger = HudPixelMod.instance();
        if(secondTry) {
            logger.logError("An API requst failed on the second try. Giving up...");
            failCause.printStackTrace();
        } else {
            logger.logWarn("An API request failed. Retrying...");
            failCause.printStackTrace();
        }
    }
    
    /**
     * Called when a request is finished to allow the next request to enter
     */
    public void unlockQueue() {
        this.isLocked = false;
    }
    
    public static Queue getInstance() {
        return instance;
    }
}
