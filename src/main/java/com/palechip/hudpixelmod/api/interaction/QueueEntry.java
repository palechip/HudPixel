package com.palechip.hudpixelmod.api.interaction;

import java.util.ArrayList;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.BoostersReply;
import net.hypixel.api.util.Callback;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;

public class QueueEntry {
    private BoosterResponseCallback boosterCallback;
    
    /**
     * This queue entry will perform a getBoosters request
     * @param callback
     */
    public QueueEntry(BoosterResponseCallback callback) {
        this.boosterCallback = callback;
    }
    
    public void run() {
        // is it a booster request?
        if(this.boosterCallback != null) {
            this.doBoosterRequest();
        }
    }
    
    private void doBoosterRequest() {
        HypixelAPI api = Queue.getInstance().getAPI();
        // do the request
        api.getBoosters(new Callback<BoostersReply>(BoostersReply.class) {
            @Override
            public void callback(Throwable failCause, BoostersReply result) {
                if(failCause != null) {
                    // if something went wrong, report it
                    Queue.getInstance().reportFailure(failCause);
                } else {
                    // assemble the response
                    ArrayList<Booster> boosters = new ArrayList<Booster>();
                    Gson gson = Queue.getInstance().getGson();
                    // the response for the booster query is an array with objects
                    // these objects are represented by the class Booster
                    for(JsonElement e : result.getBoosters()) {
                        Booster b = gson.fromJson(e, Booster.class);
                        boosters.add(b);
                    }
                    // pass the result
                    boosterCallback.onBoosterResponse(boosters);
                    // open the way for the next request
                    Queue.getInstance().unlockQueue();
                }
            }
        });
    }
}
