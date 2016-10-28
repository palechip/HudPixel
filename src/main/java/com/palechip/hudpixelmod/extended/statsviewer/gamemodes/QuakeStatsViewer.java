package com.palechip.hudpixelmod.extended.statsviewer.gamemodes;

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer;
import com.palechip.hudpixelmod.stats.StatsDisplayer;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.util.LoggerHelper.logInfo;
import static java.lang.Math.floor;

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
public class QuakeStatsViewer extends StatsDisplayer implements IGameStatsViewer {


    /*
     *Lets add some static finals. Players love static finals.
     */
    private static final String TRIGGER = D_GRAY + " [" + GRAY + "Trigger speed" + D_GRAY + "] ";
    private static final String SOLO_KD = D_GRAY + " [" + GRAY + "Solo K/D" + D_GRAY + "] ";
    private static final String TEAM_KD = D_GRAY + " [" + GRAY + "Team K/D" + D_GRAY + "] ";
    private static final String SOLO_KILLS = D_GRAY + " [" + GRAY + "Solo kills" + D_GRAY + "] ";
    private static final String SOLO_DEATHS = D_GRAY + " [" + GRAY + "Solo deaths" + D_GRAY + "] ";
    private static final String TEAM_KILLS = D_GRAY + " [" + GRAY + "Team kills" + D_GRAY + "] ";
    private static final String TEAM_DEATHS = D_GRAY + " [" + GRAY + "Team deaths" + D_GRAY + "] ";
    private static final String SOLO_WINS = D_GRAY + " [" + GRAY + "Solo wins" + D_GRAY + "] ";
    private static final String TEAM_WINS = D_GRAY + " [" + GRAY + "Team wins" + D_GRAY + "] ";
    private ArrayList<String> renderList;
    private int solo_kills;
    private int team_kills;
    private int solo_deaths;
    private int team_deaths;
    private int solo_wins;
    private int team_wins;
    private float trigger;
    private double team_kd;
    private double solo_kd;

    public QuakeStatsViewer(String playerName) {
        super(playerName);
        renderList = new ArrayList<String>();
    }

    @Override
    public ArrayList<String> getRenderList() {
        if (renderList.isEmpty()) {
            return null;
        }
        return renderList;
    }

    @Override
    protected void displayStats() {
        composeStats();
    }

    private void generateRenderList() {
        renderList.add(SOLO_WINS + GOLD + this.solo_wins + TEAM_WINS + GOLD + this.team_wins);
        renderList.add(TEAM_KILLS + GOLD + this.team_kills + TEAM_DEATHS + GOLD + this.team_deaths + TEAM_KD + GOLD + this.team_kd);
        renderList.add(SOLO_KILLS + GOLD + this.solo_kills + SOLO_DEATHS + GOLD + this.solo_deaths + SOLO_KD + GOLD + this.solo_kd);
        renderList.add(TRIGGER + GOLD + this.trigger);
    }

    public void composeStats() {

        this.solo_kills = getInt("kills");
        this.team_kills = getInt("kills_teams");
        this.solo_deaths = getInt("deaths");
        this.team_deaths = getInt("deaths_teams");
        this.solo_wins = getInt("wins");
        this.team_wins = getInt("wins_teams");

        if (solo_deaths > 0) {
            this.solo_kd = floor((solo_kills / solo_deaths) * 1000) / 1000;
        } else {
            this.solo_kd = 1;
        }
        if (this.team_deaths > 0) {
            this.team_kd = floor((team_kills / team_deaths) * 1000) / 1000;
        } else {
            this.team_kd = 1;
        }

        this.trigger = getTrigger(getString("trigger"));

        generateRenderList();

    }

    private int getInt(String s) {
        try {
            return this.statistics.get("Quake").getAsJsonObject().get(s).getAsInt();
        } catch (Exception ex) {
            logInfo("[Stats.Quake.Int]: No entry for " + s + "returning 0!");
            return 0;
        }
    }

    private String getString(String s) {
        try {
            return this.statistics.get("Quake").getAsJsonObject().get(s).getAsString();
        } catch (Exception ex) {
            logInfo("[Stats.Quake.String]: No entry for " + s + "returning Err!");
            return "Err";
        }
    }

    private float getTrigger(String trigger) {

        /**
         * Welcome to language Level 6 .... switching strings is not working here
         **/
        if (trigger.equals("NINE_POINT_ZERO")) {
            return 9.0f;
        } else if (trigger.equals("FIVE_POINT_ZERO")) {
            return 5.0f;
        } else if (trigger.equals("TWO_POINT_FIVE")) {
            return 2.5f;
        } else if (trigger.equals("ONE_POINT_FIVE")) {
            return 1.5f;
        } else if (trigger.equals("ONE_POINT_FOUR")) {
            return 1.4f;
        } else if (trigger.equals("ONE_POINT_THREE")) {
            return 1.3f;
        } else if (trigger.equals("ONE_POINT_TWO")) {
            return 1.2f;
        } else if (trigger.equals("ONE_POINT_ONE")) {
            return 1.1f;
        } else if (trigger.equals("ONE_POINT_ZERO")) {
            return 1.0f;
        } else if (trigger.equals("ZERO_POINT_NINE")) {
            return 0.9f;
        } else {
            return 666f;
        }
    }
}
