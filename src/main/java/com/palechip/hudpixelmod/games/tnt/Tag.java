package com.palechip.hudpixelmod.games.tnt;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Tag extends Game {
    public Tag() {
        super("TNT Tag", "", "The TNT has been released!", "You earned a total of");
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.tntCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }
}
