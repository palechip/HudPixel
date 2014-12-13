package com.palechip.hudpixelmod.detectors;

import com.palechip.hudpixelmod.games.Game;

public class GameStartStopDetector {
    private GameDetector gameDetector;
    private boolean detectedStartBeforeGameDetection;

    public GameStartStopDetector(GameDetector gameDetector) {
        this.gameDetector = gameDetector;
    }
    public void onChatMessage(String textMessage, String formattedMessage) {
        // filter chat messages
        if(!this.isChatMessage(textMessage)) { 
            if(this.gameDetector.getCurrentGame() != null) {
                // check for starting
                if(!this.gameDetector.getCurrentGame().hasGameStarted() && !this.gameDetector.getCurrentGame().getStartMessage().equals(Game.GAME_DETECTION_HELPER)) {
                    if (textMessage.contains(this.gameDetector.getCurrentGame().getStartMessage())) {
                        this.gameDetector.getCurrentGame().startGame();
                    }
                    // check if we missed the start message
                    if(this.detectedStartBeforeGameDetection) {
                        this.detectedStartBeforeGameDetection = false;
                        this.gameDetector.getCurrentGame().startGame();
                    }
                }
                // check for ending
                else {
                    // we don't want guild coins triggering the end message
                    if (!textMessage.toLowerCase().contains("guild coins") && textMessage.contains(this.gameDetector.getCurrentGame().getEndMessage())) {
                        this.gameDetector.getCurrentGame().endGame();
                    }
                }
            }
            // check if the game started before it was detected (I'm not sure if this is even possible)
            else if(this.gameDetector.isGameDetectionStarted()) {
                for(Game game : Game.getGames()) {
                    if(!game.getStartMessage().isEmpty() && textMessage.contains(game.getStartMessage())) {
                        this.detectedStartBeforeGameDetection = true;
                        break;
                    }
                }
            }
        }
    }
    
    private boolean isChatMessage(String textMessage) {
        try {
            // normal chat message
            if(textMessage.split(" ")[0].endsWith(":") && !textMessage.split(" ")[0].endsWith("]:")) {
                return true;
            }
            // normal chat message when the player has a rank
            if(textMessage.split(" ")[1].endsWith(":") && !textMessage.split(" ")[1].endsWith("]:")) {
                return true;
            }
        } catch(Exception e) {
        }
        // other chat channels. ("staff" is just a guess)
        if(textMessage.toLowerCase().startsWith("guild") || textMessage.toLowerCase().startsWith("staff") || textMessage.toLowerCase().startsWith("party")) {
            return true;
        }
        return false;
    }
}
