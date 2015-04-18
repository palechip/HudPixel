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
package com.palechip.hudpixelmod.util;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

/**
 * A helper which reads the scoreboard.
 * @author palechip
 *
 */
public class ScoreboardReader {
    private static boolean needsUpdate = true;
    private static ArrayList<String> scoreboardNames;
    private static String scoreboardTitle;
    
    /**
     * Used to make sure that we maximally update the scoreboard names once a tick.
     */
    public static void resetCache() {
        needsUpdate = true;
    }
    
    /**
     * Get the top-most string which is displayed on the scoreboard. (doesn't have a score on the side)
     */
    public static String getScoreboardTitle() {
        return scoreboardTitle;
    }
    
    /**
     * Get all currently visible strings on the scoreboard. (excluding title)
     */
    public static ArrayList<String> getScoreboardNames() {
        // the array will only be updated upon request
        if(needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardNames;
    }
    
    
    private static void updateNames() {
        // All the magic happens here...
        
        // Clear the array
        if(!scoreboardNames.isEmpty()) {
            scoreboardNames.clear();
        }
        
        // Get the scoreboard.
        Scoreboard scoreboard = FMLClientHandler.instance().getClient().theWorld.getScoreboard();
        // Get the right objective. I think the 1 stands for the sidebar objective but I've just copied it from the rendering code.
        ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
        scoreboardTitle = sidebarObjective.getDisplayName();
        // Get a collection of all scores
        Collection scores = scoreboard.getSortedScores(sidebarObjective);
        // Process all scores
        for(Object score : scores) {
            // Make sure it can be casted to Score
            if(score instanceof Score) {
                // Get the team for the fake player
                ScorePlayerTeam team = scoreboard.getPlayersTeam(((Score)score).getPlayerName());
                // Add the name to the array. formatPlayerName() is used to add prefix and suffix which are used to circumvent the 16 char limit for the name.
                scoreboardNames.add(ScorePlayerTeam.formatPlayerName(team, ((Score)score).getPlayerName()));
            }
        }
    }
    
    static {
        scoreboardNames = new ArrayList<String>();
    }
    
    // prevent instantiation
    private ScoreboardReader(){}
}
