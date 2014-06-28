package com.palechip.hudpixelmod.games.arcade;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.games.Game;

public class FarmHunt extends Game {

    public FarmHunt() {
        super("", "Farm Hunt", "1 seconds until the game starts!", "You earned a total of");
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
