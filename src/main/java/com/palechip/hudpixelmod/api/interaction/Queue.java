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
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.callbacks.*;
import com.palechip.hudpixelmod.config.GeneralConfigSettings;
import net.hypixel.api.HypixelAPI;

import java.util.ArrayList;
import java.util.UUID;

public class Queue implements ApiKeyLoadedCallback {

    // current values limit the queue to 1 request every 5 seconds on average
    private static final int HEAT_PER_REQUEST = 5;
    private static final int HEAT_MAXIMUM = 100;
    private static final int HEAT_COOLDOWN_PER_SECOND = 1;
    private static final int MIN_TIME_BETWEEN_KEY_NOTICES = 60000; // = 1 min
    private static final int API_DISABLED_TIMEOUT = 15000;
    private static Queue instance;
    public boolean apiEnabled = false;
    private ApiKeyHandler keyHandler;
    private HypixelAPI api;
    // even though the queue doesn't need it, it holds a gson instance which is used by QueueEntry
    private Gson gson;
    private ArrayList<QueueEntry> queue;
    private boolean isLocked;
    // used to limit the amount of requests, every request produces heat
    private int heat;
    // keeps track of last second, so it can reduce the heat
    private long sec;
    // saves when the mod last asked the user for a key
    private long lastKeyNotice;

    public Queue() {
        instance = this;
        this.queue = new ArrayList<QueueEntry>();
        // Instantiate the API first, so the key can't be loaded when the api is still null
        this.api = HypixelAPI.getInstance();
        this.gson = new Gson();
        this.keyHandler = new ApiKeyHandler(this);
    }

    public static Queue getInstance() {
        return instance;
    }

    public void onChatMessage(String textMessage) {
        this.keyHandler.onChatMessage(textMessage);
    }

    public void onClientTick() {
        // request the key if necessary
        if (GeneralConfigSettings.getUseAPI() && !this.apiEnabled && !this.queue.isEmpty()) {
            // if the api is disabled, the requests will expire
            if (this.queue.get(0).getCreationTime() + API_DISABLED_TIMEOUT < System.currentTimeMillis()) {
                this.queue.get(0).cancel();
                this.queue.remove(0);
            }
            if (this.lastKeyNotice == 0 || System.currentTimeMillis() > this.lastKeyNotice + MIN_TIME_BETWEEN_KEY_NOTICES) {
                this.lastKeyNotice = System.currentTimeMillis();
                ApiKeyHandler.requestApiKey();
            }
        }

        // run the queue
        if (GeneralConfigSettings.getUseAPI() && this.apiEnabled && !this.queue.isEmpty() && !this.isLocked && this.heat < HEAT_MAXIMUM + HEAT_PER_REQUEST) {
            QueueEntry entry = this.queue.get(0);
            entry.run();
            this.queue.remove(0);
            this.heat += HEAT_PER_REQUEST;
        }

        long currentSec = System.currentTimeMillis() / 1000;
        if (this.sec < currentSec) {
            this.heat -= HEAT_COOLDOWN_PER_SECOND * (currentSec - this.sec);
            this.sec = currentSec;
        }
    }

    @Override
    public void ApiKeyLoaded(boolean failed, String key) {
        if (failed) {
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
     * Queues a BoosterExtended request.
     * @param callback
     */
    public void getBoosters(BoosterResponseCallback callback) {
        if (GeneralConfigSettings.getUseAPI()) {
            this.queue.add(new QueueEntry(callback));
        } else {
            ApiKeyHandler.requestApiKey();
            callback.onBoosterResponse(null);
        }
    }

    /**
     * Queues a Session request.
     * @param callback
     */
    public void getSession(SessionResponseCallback callback, String player, Boolean viaUUID) {
        if (GeneralConfigSettings.getUseAPI()) {
            this.queue.add(new QueueEntry(callback, player, viaUUID));
        } else {
            ApiKeyHandler.requestApiKey();
            callback.onSessionRespone(null);
        }
    }

    /**
     * Queues a Friends request.
     * @param callback
     */
    public void getFriends(FriendResponseCallback callback, String player) {
        if (GeneralConfigSettings.getUseAPI()) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            ApiKeyHandler.requestApiKey();
            callback.onFriendResponse(null);
        }
    }

    /**
     * Queues a Friends request.
     * @param callback
     */
    public void getFriends(FriendResponseCallback callback, UUID player) {
        if (GeneralConfigSettings.getUseAPI()) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            ApiKeyHandler.requestApiKey();
            callback.onFriendResponse(null);
        }
    }

    /**
     * Queues a Player request.
     * @param callback
     * @param player
     */
    public void getPlayer(PlayerResponseCallback callback, String player) {
        if (GeneralConfigSettings.getUseAPI()) {
            this.queue.add(new QueueEntry(callback, player));
        } else {
            ApiKeyHandler.requestApiKey();
            callback.onPlayerResponse(null);
        }
    }

    public void reportFailure(Throwable failCause, boolean secondTry) {
        HudPixelMod logger = HudPixelMod.instance();
        if (secondTry) {
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
}
