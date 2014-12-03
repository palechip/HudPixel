package com.palechip.hudpixelmod.games.arcade;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class EnderSpleef extends Game {

    public EnderSpleef() {
        super("", "Ender Spleef", "1 seconds until the game starts!", "You earned a total of");
        if(HudPixelConfig.arcadeTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }

}
