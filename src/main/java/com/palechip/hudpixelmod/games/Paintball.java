package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.PaintballKillstreakTimer;
import com.palechip.hudpixelmod.components.TimerComponent;

public class Paintball extends Game {

    protected Paintball() {
        super("PB", "", "1 seconds until the game starts!", "You earned a total of");
        if(HudPixelConfig.paintballTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.paintballCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.paintballKillstreakTimerDisplay) {
            // Triple Shot
            this.components.add(new PaintballKillstreakTimer("Triple Shot"));
            // Strong Arm
            this.components.add(new PaintballKillstreakTimer("Strong Arm"));
            // Forcefield
            this.components.add(new PaintballKillstreakTimer("Forcefield"));
            // Super Strong Arm
            this.components.add(new PaintballKillstreakTimer("Super Strong Arm"));
            // Rambo
            this.components.add(new PaintballKillstreakTimer("RAMBO"));
        }
    }

}
