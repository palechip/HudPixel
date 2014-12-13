package com.palechip.hudpixelmod.detectors;

import java.util.ArrayList;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.util.ScoreboardReader;

import net.hypixel.api.util.GameType;
import net.minecraft.util.EnumChatFormatting;

public class ArcadeGamesDetector extends Game{

    /**
     * This is a dummy game which helps to detect the actual game.
     */
    public ArcadeGamesDetector() {
        super("", "ARCADE GAMES", GAME_DETECTION_HELPER, GAME_DETECTION_HELPER, GameType.ARCADE);
    }

    @Override
    public void setupNewGame() {
        // Read the scoreborad to find out which game it is.
        ArrayList<String> names = ScoreboardReader.getScoreboardNames();
        String name = null;
        // find the map name which is the game name
        for(String s : names) {
            if(s.contains("Map: ")) {
                // Get rid of the map part + the color code ("Map: "(5 chars) + color code (2 chars)
                name = s.substring(7);
            }
        }

        // did we fail?
        if(name == null) {
            HudPixelMod.instance().logError("Failed to detect the Arcade Game which is being played. :(");
            return;
        }

        // find the correct game
        for(Game game : Game.getGames()) {
            if(game.getType() == GameType.ARCADE && game.getBossbarName().equals(name)) {
                HudPixelMod.instance().gameDetector.currentGame = game;
                game.setupNewGame();
                break;
            }
        }
    }
}
