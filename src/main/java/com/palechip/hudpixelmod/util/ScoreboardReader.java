package com.palechip.hudpixelmod.util;

import java.util.ArrayList;
import java.util.Collection;

import cpw.mods.fml.client.FMLClientHandler;
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
    
    /**
     * Used to make sure that we maximally update the scoreboard names once a tick.
     */
    public static void resetCache() {
        needsUpdate = true;
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
        ScoreObjective sidebarObjective = scoreboard.func_96539_a(1);
        // Get a collection of all scores
        Collection scores = scoreboard.func_96534_i(sidebarObjective);
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
