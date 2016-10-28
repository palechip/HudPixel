/***********************************************************************************************************************
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

package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.api.interaction.callbacks.SessionResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Session;
import com.palechip.hudpixelmod.extended.util.ILoadPlayerHeadCallback;
import com.palechip.hudpixelmod.extended.util.LoadPlayerHead;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import net.minecraft.util.ResourceLocation;


public class OnlineFriend extends FancyListObject implements ILoadPlayerHeadCallback, SessionResponseCallback, McColorHelper {


    private String username;
    private String gamemode;
    private boolean isOnline;
    private String friendUUID;

    /**
     * Constructor ... also loads the playerhead
     *
     * @param username Username
     * @param gamemode current string to render
     */
    OnlineFriend(String username, String gamemode, String uuid) {
        this.gamemode = gamemode;
        this.username = username;
        this.friendUUID = uuid;

        this.resourceLocation = null;

        this.renderLine1 = GRAY + "o " + GOLD + username;
        this.renderLine2 = GRAY + gamemode;
        this.renderLineSmall = GRAY + "o " + YELLOW + username;
        this.renderPicture = WHITE + "";

        new LoadPlayerHead(username, this);
    }

    String getUsername() {
        return username;
    }

    String getGamemode() {
        return gamemode;
    }

    void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public boolean isOnline() {
        return isOnline;
    }

    void setOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }


    @Override
    public void onTick() {
        if (!OnlineFriendManager.enabled) return;
        if (isOnline && this.fancyListObjectButtons.isEmpty()) {
            this.addButton(new OnlineFriendsMessageButton(username));
            this.addButton(new OnlineFriendsPartyButton(username));
        } else if (!isOnline) {
            this.fancyListObjectButtons.clear();
        }
        if (isOnline) this.renderLine1 = GOLD + username + GREEN + "  ";
        else this.renderLine1 = GOLD + username + D_RED + "  ";
        this.renderLine2 = gamemode;
        if (isOnline) this.renderLineSmall = YELLOW + username + GREEN + "  ";
        else this.renderLineSmall = YELLOW + username + D_RED + " ";
    }

    @Override
    public void onLoadPlayerHeadResponse(ResourceLocation resourceLocation) {
        if (resourceLocation != null) {
            this.resourceLocation = resourceLocation;
        }
    }

    @Override
    public void onSessionRespone(Session session) {
        System.out.println(session.toString());
        gamemode = GREEN + session.getReplyGameType();
    }
}



