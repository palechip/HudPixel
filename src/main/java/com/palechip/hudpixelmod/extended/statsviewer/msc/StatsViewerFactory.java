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

package com.palechip.hudpixelmod.extended.statsviewer.msc;

import com.palechip.hudpixelmod.extended.statsviewer.gamemodes.*;
import com.palechip.hudpixelmod.util.GameType;

import java.util.UUID;

public class StatsViewerFactory {

    /**
     * Not really a factory ... but at least it generates and returns teh right class
     * for the given gametype ... i will maybe switch to a real design pattern
     *
     * @param gameType the gametype
     * @return the right statsViewer for the given player and gametype
     */
    public static IGameStatsViewer getStatsViewerClass(UUID uuid, GameType gameType) {
        switch (gameType) {
            case WARLORDS:
                return new WarlordsStatsViewer(uuid, gameType.getStatsName());
            case QUAKECRAFT:
                return new QuakeStatsViewer(uuid, gameType.getStatsName());
            case PAINTBALL:
                return new PaintballStatsViewer(uuid, gameType.getStatsName());
            case THE_WALLS:
                return new WallsStatsViewer(uuid, gameType.getStatsName());
            case VAMPIREZ:
                return new VampireStatsViewer(uuid, gameType.getStatsName());
            case COPS_AND_CRIMS:
                return new CvCStatsViewer(uuid, gameType.getStatsName());
            case UHC:
                return new UHCStatsViewer(uuid, gameType.getStatsName());
            case TNT_GAMES:
            case TNT_RUN:
            case TNT_TAG:
            case TNT_WIZARDS:
                return new TNTStatsViewer(uuid, gameType.getStatsName());
            case BLITZ:
                return new BlitzStatsViewer(uuid, gameType.getStatsName());
            case ARCADE_GAMES:
                return new ArcadeStatsViewer(uuid, gameType.getStatsName());
            case ARENA:
                return new ArenaStatsViewer(uuid, gameType.getStatsName());
            case MEGA_WALLS:
                return new MegaWallsStatsViewer(uuid, gameType.getStatsName());
            default:
                return new NullStatViewer();
        }
    }
}
