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
package com.palechip.hudpixelmod.api.interaction.representations;

import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.util.UuidCallback;
import com.palechip.hudpixelmod.util.UuidHelper;


public class Booster implements UuidCallback {

    // these values are filled by the API
    // purchaser is onl filled for old boosters
    public static final int TIPPING_COOLDOWN = 1800000; // = 30 min

    private int amount;
    private long dateActivated;
    private int gameType;
    private long length;
    private long originalLength;
    private String purchaserUuid;

    // properties used by the mod
    private long tippingTime;
    private String owner;
    private int gameID; // saves the ID the mod uses for the game and not the database ID.

    public Booster() {
    }

    // allows boosters to be created from the /booster queue command
    public Booster(String name, int gameID) {
        this.purchaserUuid = ""; // the UUID isn't necessary
        this.owner = name;
        this.gameID = gameID;
        this.originalLength = 60 * 60; // one hour
        // we can't know the length but the booster must be active
        this.length = this.originalLength - 1;
        // neither can we know the activation time
        this.dateActivated = System.currentTimeMillis();
        // get the db id
        this.gameType = GameType.getTypeByID(gameID).getModID();
    }

    public String getTipName() {
        return GameType.getTypeByDatabaseID(gameType).getTipName();
    }

    public int getCoinAmount() {
        return amount;
    }

    public long getActivationDateAndTime() {
        return dateActivated;
    }

    /**
     * Returns the mod ID for the game which will can be used to get the game configuration
     * @return
     */
    public int getGameID() {
        if (this.gameID == 0) {
            // go through all configurations
            for (GameType config : GameType.values()) {
                // if we find one with a matching database id
                if (config.getModID() == this.gameType) {
                    // save it
                    this.gameID = config.getModID();
                }
            }
        }
        return this.gameID;
    }

    public long getRemainingTime() {
        return length;
    }

    public long getTotalLength() {
        return originalLength;
    }

    public String getOwner() {
        if (this.owner == null || this.owner.equals("!ERROR!")) {
            new UuidHelper(purchaserUuid, this);
        }
        // did getting the name fail?
        if (this.owner == null) {
            this.owner = "!ERROR!";
        }
        return owner;
    }

    /**
     * This saves the time when the booster was tipped.
     */
    public void tip() {
        this.tippingTime = System.currentTimeMillis();
    }

    public boolean isTipped() {
        // only if the booster is already active
        if (this.getRemainingTime() < this.getTotalLength()) {
            // this expression is true if there is a tipping time and it's less than half an hour ago
            return ((this.tippingTime != 0l && System.currentTimeMillis() < this.tippingTime + TIPPING_COOLDOWN));
        } else {
            return false;
        }
    }

    public void setTippingTime(long time) {
        this.tippingTime = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Booster) {
            Booster b = (Booster) obj;
            return this.owner.equals(b.owner) && this.gameType == b.gameType;
        }
        return super.equals(obj);
    }

    @Override
    public void onUuidCallback(String playerName) {
        this.owner = playerName;
    }
}
