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

package eladkay.hudpixel.util;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A helper which reads the scoreboard.
 *
 * @author palechip
 */
public class ScoreboardReader {
    private static boolean needsUpdate = true;
    private static ArrayList<String> scoreboardNames;
    private static String scoreboardTitle;

    static {
        scoreboardNames = new ArrayList<String>();
        scoreboardTitle = "";
    }

    // prevent instantiation
    private ScoreboardReader() {
    }

    /**
     * Used to make sure that we maximally update the scoreboard names once a tick.
     */
    public static void resetCache() {
        needsUpdate = true;
    }

    /**
     * Get the top-most string which is displayed on the scoreboard. (doesn't have a score on the side)
     * Be aware that this can contain color codes.
     */
    public static String getScoreboardTitle() {
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardTitle;
    }

    /**
     * Get all currently visible strings on the scoreboard. (excluding title)
     * Be aware that this can contain color codes.
     */
    public static ArrayList<String> getScoreboardNames() {
        // the array will only be updated upon request
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardNames;
    }

    private static void updateNames() {
        // All the magic happens here...

        // Clear the array
        if (!scoreboardNames.isEmpty()) {
            scoreboardNames.clear();
        }
        scoreboardTitle = "";

        try {
            // Get the scoreboard.
            Scoreboard scoreboard = FMLClientHandler.instance().getClient().world.getScoreboard();
            // Get the right objective. I think the 1 stands for the sidebar objective but I've just copied it from the rendering code.
            ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
            // only update if there actually is something to update
            scoreboardTitle = sidebarObjective.getDisplayName();
            // Get a collection of all scores
            Collection<Score> scores = scoreboard.getSortedScores(sidebarObjective);
            // Process all scores
            for (Score score : scores) {
                // Get the team for the fake player
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                // Add the nm to the array. formatPlayerName() is used to add prefix and suffix which are used to circumvent the 16 char limit for the nm.
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));

            }
        } catch (Exception e) {
            // it is possible that there is a null pointer exception thrown when there is no scoreboard
            // just ignore this
        }
    }
}
