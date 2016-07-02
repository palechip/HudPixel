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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.FriendResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.PlayerResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.SessionResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.api.interaction.representations.Friend;
import com.palechip.hudpixelmod.api.interaction.representations.Player;
import com.palechip.hudpixelmod.api.interaction.representations.Session;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.BoostersReply;
import net.hypixel.api.reply.FriendsReply;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.reply.SessionReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;

import java.util.ArrayList;

public class QueueEntry {
    private boolean isSecondTry;
    private long creationTime;
    
    private BoosterResponseCallback boosterCallback;
    private SessionResponseCallback sessionCallback;
    private FriendResponseCallback friendCallback;
    private PlayerResponseCallback playerCallback;
    private String player;
    
    /**
     * This queue entry will perform a getBoosters request
     * @param callback
     */
    public QueueEntry(BoosterResponseCallback callback) {
        this.boosterCallback = callback;
        this.creationTime = System.currentTimeMillis();
    }
    
    /**
     * This queue entry will perform a getSession request
     * @param callback
     */
    public QueueEntry(SessionResponseCallback callback, String player) {
        this.sessionCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
    }
    
    /**
     * This queue entry will perform a getFriends request
     * @param callback
     */
    public QueueEntry(FriendResponseCallback callback, String player) {
        this.friendCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
    }
    
    public QueueEntry(PlayerResponseCallback callback, String player) {
        this.playerCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
    }
    
    public void run() {
        // is it a booster request?
        if(this.boosterCallback != null) {
            this.doBoosterRequest();
        } else if(this.sessionCallback != null) {
            this.doSessionRequest();
        } else if(this.friendCallback != null) {
            this.doFriendRequest();
        } else if(this.playerCallback != null) {
            this.doPlayerRequest();
        }
    }
    
    private void failed(Throwable failCause) {
        Queue.getInstance().reportFailure(failCause, this.isSecondTry);
        if(this.isSecondTry) {
            this.cancel();
        } else {
            // retry
            this.isSecondTry = true;
            this.run();
        }
    }
    
    public void cancel() {
        if(this.boosterCallback != null) {
            this.boosterCallback.onBoosterResponse(null);
        } else if(this.sessionCallback != null) {
            this.sessionCallback.onSessionRespone(null);
        } else if(this.friendCallback != null) {
            this.friendCallback.onFriendResponse(null);
        }
        // open the way for the next request
        Queue.getInstance().unlockQueue();
    }
    
    public long getCreationTime() {
        return this.creationTime;
    }
    
    private void doBoosterRequest() {
        HypixelAPI api = Queue.getInstance().getAPI();

        Request request = RequestBuilder.newBuilder(RequestType.BOOSTERS)
                .createRequest();

        // do the request
        api.getAsync(request, new Callback<BoostersReply>(BoostersReply.class){

            @Override
            public void callback(Throwable failCause, BoostersReply result) {
                if(failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    ArrayList<Booster> boosters = new ArrayList<Booster>();
                    Gson gson = Queue.getInstance().getGson();
                    // the response for the booster query is an array with objects
                    // these objects are represented by the class BoosterExtended
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
    
    private void doSessionRequest() {
        HypixelAPI api = Queue.getInstance().getAPI();
        // do the request

        Request request = RequestBuilder.newBuilder(RequestType.SESSION)
                .addParam(RequestParam.SESSION_BY_NAME, this.player)
                .createRequest();

        api.getAsync(request, new Callback<SessionReply>(SessionReply.class) {
            @Override
            public void callback(Throwable failCause, SessionReply result) {
                if (failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    Gson gson = Queue.getInstance().getGson();
                    // the class session represents the entire request
                    Session s = gson.fromJson(result.getSession(), Session.class);
                    if(s == null) {
                        // there was no session, so lets return a session with everything null except the player
                        s = new Session();
                    }
                    s.setSessionOwner(player);
                    // pass the result
                    sessionCallback.onSessionRespone(s);
                    // open the way for the next request
                    Queue.getInstance().unlockQueue();
                }
            }
        });
    }
    
    private void doFriendRequest() {
        HypixelAPI api = Queue.getInstance().getAPI();

        Request request = RequestBuilder.newBuilder(RequestType.FRIENDS)
                .addParam(RequestParam.FRIENDS_BY_NAME, this.player)
                .createRequest();

        // do the request
        api.getAsync(request, new Callback<FriendsReply>(FriendsReply.class){
            
            @Override
            public void callback(Throwable failCause, FriendsReply result) {
                if (failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    ArrayList<Friend> friends = new ArrayList<Friend>();
                    Gson gson = Queue.getInstance().getGson();
                    // the response for the booster query is an array with objects
                    // these objects are represented by the class BoosterExtended
                    for(JsonElement e : result.getRecords()) {
                        Friend f = gson.fromJson(e, Friend.class);
                        f.setPlayer(player);
                        friends.add(f);
                    }
                    // pass the result
                    friendCallback.onFriendResponse(friends);
                    // open the way for the next request
                    Queue.getInstance().unlockQueue();
                }
            }
        });
    }
    
    private void doPlayerRequest() {
        HypixelAPI api = Queue.getInstance().getAPI();

        Request request = RequestBuilder.newBuilder(RequestType.PLAYER)
                .addParam(RequestParam.PLAYER_BY_NAME, this.player)
                .createRequest();

        // do the request
        api.getAsync(request, new Callback<PlayerReply>(PlayerReply.class){

            @Override
            public void callback(Throwable failCause, PlayerReply result) {
                if(failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    Gson gson = Queue.getInstance().getGson();
                    Player player = gson.fromJson(result.getPlayer(), Player.class);
                    // pass the result
                    playerCallback.onPlayerResponse(player);
                    // open the way for the next request
                    Queue.getInstance().unlockQueue();
                }
            }
        });
    }
}
