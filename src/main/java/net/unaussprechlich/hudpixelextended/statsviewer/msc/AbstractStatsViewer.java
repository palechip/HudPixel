/*
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
 */

package net.unaussprechlich.hudpixelextended.statsviewer.msc;

import com.google.gson.JsonObject;
import eladkay.hudpixel.util.ChatMessageComposer;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.util.text.TextFormatting;
import net.unaussprechlich.hudpixelextended.hypixelapi.ApiQueueEntryBuilder;
import net.unaussprechlich.hudpixelextended.hypixelapi.callbacks.PlayerResponseCallback;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;

import java.util.ArrayList;
import java.util.UUID;

import static net.unaussprechlich.hudpixelextended.util.LoggerHelper.logInfo;

public abstract class AbstractStatsViewer implements IGameStatsViewer, PlayerResponseCallback {

    protected ArrayList<String> renderList;
    protected final UUID playerUUID;
    protected final String databaseName;
    protected JsonObject statistics;

    protected AbstractStatsViewer(UUID uuid, String databaseName) {
        this.playerUUID = uuid;
        this.databaseName = databaseName;
        renderList = new ArrayList<>();
        ApiQueueEntryBuilder.newInstance().playerRequestByUUID(uuid).setCallback(this).create();
    }


    /**
     * Implements the IGameStatsViewer interface
     *
     * @return the renderList
     */
    @Override
    public ArrayList<String> getRenderList() {
        if (renderList.isEmpty()) {
            return null;
        }
        return renderList;
    }

    protected abstract void composeStats();

    protected double calculateKD(int kills, int deaths) {
        if (deaths > 0)
            return (double) Math.round(((double) kills / (double) deaths) * 1000) / 1000;
        else
            return kills;
    }

    protected int calculateWL(int wins, int losses) {
        if (losses > 0)
            return (int) Math.round(((double) wins / (double) (wins + losses)) * 100);
        else
            return 100;
    }

    /**
     * little helper function that gets the Json entry
     *
     * @param s entry in the "Warlords" object
     * @return 0 if the entry is null
     */
    protected int getInt(String s) {
        try {
            return this.statistics.get(s).getAsInt();
        } catch (Exception ex) {
            logInfo("[Stats| + " + databaseName + "]: No entry for " + s + "returning 0!");
            return 0;
        }
    }

    protected String getString(String s) {
        try {
            return this.statistics.get(s).getAsString();
        } catch (Exception ex) {
            logInfo("[Stats| + " + databaseName + "]: No entry for " + s + "returning Err!");
            return "Err";
        }
    }

    @Override
    public void onPlayerResponse(PlayerReply player) {
        if (player != null && player.getPlayer() != null) {
            LoggerHelper.logInfo("[Stats]loaded stats for uuid=" + playerUUID);
            this.statistics = player.getPlayer().get("stats").getAsJsonObject().get(databaseName).getAsJsonObject();
            composeStats();
            return;
        }
        new ChatMessageComposer("Failed to load stats for: " + playerUUID + "!", TextFormatting.RED).send();
        renderList.add(RED + "FAILED!");
    }
}
