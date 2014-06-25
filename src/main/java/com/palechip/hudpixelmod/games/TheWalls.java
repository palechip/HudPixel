package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;

public class TheWalls extends Game {

    public TheWalls() {
        super("Walls", "", "The game has started! You're on team ", "You earned a total of");
        if(HudPixelConfig.wallsTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.wallsCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
