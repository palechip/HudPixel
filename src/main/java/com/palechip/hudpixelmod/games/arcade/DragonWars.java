package com.palechip.hudpixelmod.games.arcade;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.KillstreakTracker;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class DragonWars extends Game {

    public DragonWars() {
        super("", "Dragon Wars", "1 seconds until the game starts!", "You earned a total of");
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.arcadeKillstreakTracker) {
            this.components.add(new KillstreakTracker());
        }
    }
}
