package com.palechip.hudpixelmod.games.tnt;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class BowSpleef extends Game {
    public BowSpleef() {
        super("Bow Spleef", "", "The game has now begun!", "You earned a total of");
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }
}
