package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.KillstreakTracker;
import com.palechip.hudpixelmod.components.TimerComponent;

public class Quakecraft extends Game {

    protected Quakecraft() {
        super("Quake", "Quakecraft", "The game is starting! Right-click to shoot with your Railgun to instakill players!", "You earned a total of");
        if(HudPixelConfig.quakeTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.quakeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.quakeKillstreakTracker) {
            this.components.add(new KillstreakTracker());
        }
    }
}
