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

package net.unaussprechlich.hudpixelextended.statsviewer.gamemodes;

import net.unaussprechlich.hudpixelextended.statsviewer.msc.AbstractStatsViewer;

import java.util.UUID;

public class WarlordsStatsViewer extends AbstractStatsViewer {

    /**
     * Some static-final stuff to make the code cleaner ....
     */
    private static final String SHA = D_GRAY + " [" + WHITE + "SHA" + D_GRAY + "] ";
    private static final String WAR = D_GRAY + " [" + WHITE + "WAR" + D_GRAY + "] ";
    private static final String PAL = D_GRAY + " [" + WHITE + "PAL" + D_GRAY + "] ";
    private static final String MAG = D_GRAY + " [" + WHITE + "MAG" + D_GRAY + "] ";
    private static final String KDA = D_GRAY + " [" + WHITE + "K" + D_GRAY + "|"
            + WHITE + "A" + D_GRAY + "|" + WHITE + "D" + D_GRAY + "] ";
    private static final String LOS = D_GRAY + " [" + WHITE + "LOS" + D_GRAY + "] ";
    private static final String WIN = D_GRAY + " [" + WHITE + "WIN" + D_GRAY + "] ";
    private static final String KD = D_GRAY + " [" + WHITE + "K/D" + D_GRAY + "] ";
    private static final String WL = D_GRAY + " [" + WHITE + "Wl" + D_GRAY + "] ";
    private int kills;
    private int assists;
    private int deaths;
    private double kd;
    private int wl;
    private int losses;
    private int wins;
    private int shamanLevel;
    private int warriorLevel;
    private int mageLevel;
    private int paladinLevel;


    /**
     * constructor for the StatsViewerFactory
     */
    public WarlordsStatsViewer(UUID uuid, String statsName) {
        super(uuid, statsName);
    }

    /**
     * generates the renderList
     */
    private void generateRenderList() {
        renderList.add(KD + GOLD + kd + WL + GOLD + wl + "%");
        renderList.add(LOS + GOLD + losses + WIN + GOLD + wins);
        renderList.add(KDA + GOLD + kills + D_GRAY + " | " + GOLD + assists + D_GRAY + " | " + GOLD + deaths);
        renderList.add(SHA + GOLD + shamanLevel + WAR + GOLD + warriorLevel + PAL + GOLD + paladinLevel + MAG + GOLD + mageLevel);
    }

    /**
     * Function to compose the Jason object into teh right variables
     */
    @Override
    protected void composeStats() {
        kills = getInt("kills");
        assists = getInt("assists");
        deaths = getInt("deaths");
        wins = getInt("wins");
        losses = getInt("losses");

        kd = calculateKD(kills, deaths);
        wl = calculateWL(wins, losses);

        shamanLevel = getInt("shaman_health") + getInt("shaman_energy") + getInt("shaman_cooldown")
                + getInt("shaman_critchance") + getInt("shaman_critmultiplier") + getInt("shaman_skill1")
                + getInt("shaman_skill2") + getInt("shaman_skill3") + getInt("shaman_skill4")
                + getInt("shaman_skill5");

        warriorLevel = getInt("warrior_health") + getInt("warrior_energy") + getInt("warrior_cooldown")
                + getInt("warrior_critchance") + getInt("warrior_critmultiplier") + getInt("warrior_skill1")
                + getInt("warrior_skill2") + getInt("warrior_skill3") + getInt("warrior_skill4")
                + getInt("warrior_skill5");

        mageLevel = getInt("mage_health") + getInt("mage_energy") + getInt("mage_cooldown")
                + getInt("mage_critchance") + getInt("mage_critmultiplier") + getInt("mage_skill1")
                + getInt("mage_skill2") + getInt("mage_skill3") + getInt("mage_skill4")
                + getInt("mage_skill5");

        paladinLevel = getInt("paladin_health") + getInt("paladin_energy") + getInt("paladin_cooldown")
                + getInt("paladin_critchance") + getInt("paladin_critmultiplier") + getInt("paladin_skill1")
                + getInt("paladin_skill2") + getInt("paladin_skill3") + getInt("paladin_skill4")
                + getInt("paladin_skill5");

        generateRenderList();
    }

}
