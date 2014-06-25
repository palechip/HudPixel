package com.palechip.hudpixelmod.games.tnt;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Run extends Game {
    public Run() {
        super("TNT Run", "", "Go!", "You earned a total of");
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }
}
