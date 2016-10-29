/* **********************************************************************************************************************
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 **********************************************************************************************************************/
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
import java.util.UUID;

public class QueueEntry {
    private boolean isSecondTry;
    private long creationTime;

    private BoosterResponseCallback boosterCallback;
    private SessionResponseCallback sessionCallback;
    private FriendResponseCallback friendCallback;
    private PlayerResponseCallback playerCallback;
    private String player;
    private UUID playerUUID;
    private Boolean viaUUID;

    /**
     * This queue entry will perform a getBoosters request
     *
     * @param callback
     */
    public QueueEntry(BoosterResponseCallback callback) {
        this.boosterCallback = callback;
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * This queue entry will perform a getSession request
     *
     * @param callback
     */
    public QueueEntry(SessionResponseCallback callback, String player, Boolean viaUUID) {
        this.sessionCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
        this.viaUUID = viaUUID;
    }

    /**
     * This queue entry will perform a getFriends request
     *
     * @param callback
     */
    public QueueEntry(FriendResponseCallback callback, String player) {
        this.friendCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
        this.playerUUID = null;
    }

    /**
     * This queue entry will perform a getFriends request
     *
     * @param callback
     */
    public QueueEntry(FriendResponseCallback callback, UUID player) {
        this.friendCallback = callback;
        this.playerUUID = player;
        this.creationTime = System.currentTimeMillis();
    }

    public QueueEntry(PlayerResponseCallback callback, String player) {
        this.playerCallback = callback;
        this.player = player;
        this.creationTime = System.currentTimeMillis();
    }

    public void run() {
        // is it a booster request?
        if (this.boosterCallback != null) {
            this.doBoosterRequest();
        } else if (this.sessionCallback != null) {
            this.doSessionRequest();
        } else if (this.friendCallback != null) {
            this.doFriendRequest();
        } else if (this.playerCallback != null) {
            this.doPlayerRequest();
        }
    }

    private void failed(Throwable failCause) {
        Queue.getInstance().reportFailure(failCause, this.isSecondTry);
        if (this.isSecondTry) {
            this.cancel();
        } else {
            // retry
            this.isSecondTry = true;
            this.run();
        }
    }

    public void cancel() {
        if (this.boosterCallback != null) {
            this.boosterCallback.onBoosterResponse(null);
        } else if (this.sessionCallback != null) {
            this.sessionCallback.onSessionRespone(null);
        } else if (this.friendCallback != null) {
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
        api.getAsync(request, new Callback<BoostersReply>(BoostersReply.class) {

            @Override
            public void callback(Throwable failCause, BoostersReply result) {
                if (failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    ArrayList<Booster> boosters = new ArrayList<Booster>();
                    Gson gson = Queue.getInstance().getGson();
                    // the response for the booster query is an array with objects
                    // these objects are represented by the class BoosterExtended
                    for (JsonElement e : result.getBoosters()) {
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

    private UUID stringToUUID(String s) {
        String uuidS = s.substring(0, 8) + "-" + s.substring(8, 12) + "-"
                + s.substring(12, 16) + "-" + s.substring(16, 20) + "-"
                + s.substring(20);
        return UUID.fromString(uuidS);
    }

    private void doSessionRequest() {

        HypixelAPI api = Queue.getInstance().getAPI();

        final Request request;
        // do the request

        if (this.viaUUID) {
            request = RequestBuilder.newBuilder(RequestType.SESSION)
                    .addParam(RequestParam.SESSION_BY_UUID, stringToUUID(player))
                    .createRequest();
        } else {
            request = RequestBuilder.newBuilder(RequestType.SESSION)
                    .addParam(RequestParam.SESSION_BY_NAME, this.player)
                    .createRequest();
        }

        api.getAsync(request, new Callback<SessionReply>(SessionReply.class) {
            @Override
            public void callback(Throwable failCause, SessionReply result) {
                if (failCause != null) {
                    // if something went wrong, handle it
                    failed(failCause);
                } else {
                    // assemble the response
                    Gson gson = Queue.getInstance().getGson();
                    System.out.println(gson.toString());
                    // the class session represents the entire request
                    Session s = gson.fromJson(result.getSession(), Session.class);
                    if (s == null) {
                        // there was no session, so lets return a session with everything null except the player
                        s = new Session();
                    }
                    if (viaUUID)
                        s.setSessionOwner(player);
                    else
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

        final Request request;
        // do the request

        if (this.playerUUID != null) {
            request = RequestBuilder.newBuilder(RequestType.FRIENDS)
                    .addParam(RequestParam.FRIENDS_BY_UUID, playerUUID)
                    .createRequest();
        } else {
            request = RequestBuilder.newBuilder(RequestType.FRIENDS)
                    .addParam(RequestParam.FRIENDS_BY_NAME, player)
                    .createRequest();
        }
        // do the request
        api.getAsync(request, new Callback<FriendsReply>(FriendsReply.class) {

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
                    for (JsonElement e : result.getRecords()) {

                        Friend f = gson.fromJson(e, Friend.class);

                        if (playerUUID != null) f.setPlayer(playerUUID);
                        else f.setPlayer(player);

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
        api.getAsync(request, new Callback<PlayerReply>(PlayerReply.class) {

            @Override
            public void callback(Throwable failCause, PlayerReply result) {
                if (failCause != null) {
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
