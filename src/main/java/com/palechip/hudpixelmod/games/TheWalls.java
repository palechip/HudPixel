package com.palechip.hudpixelmod.games;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;

public class TheWalls extends Game {

    public TheWalls() {
        super("Walls", "Walls", "The game has started! You're on team ", END_MESSAGE_DEFAULT, GameType.WALLS);
        if(HudPixelConfig.wallsTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.wallsCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
