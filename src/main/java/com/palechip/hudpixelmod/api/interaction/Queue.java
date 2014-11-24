package com.palechip.hudpixelmod.api.interaction;

import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.Gson;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.callbacks.ApiKeyLoadedCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.SessionResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.api.interaction.representations.Session;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.Callback;

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
        // run the queue
        if(!this.queue.isEmpty() && !this.isLocked && this.heat < HEAT_MAXIMUM + HEAT_PER_REQUEST) {
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
        this.queue.add(new QueueEntry(callback));
    }
    
    /**
     * Queues a Session request.
     * @param callback
     */
    public void getSession(SessionResponseCallback callback, String player) {
        this.queue.add(new QueueEntry(callback, player));
    }
    
    public void reportFailure(Throwable failCause) {
        //TODO
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
