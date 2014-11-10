package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.BlitzDeathmatchNotifier;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Blitz extends Game {

    public Blitz() {
        super("B-SG", "Blitz SG", "You will get the items for your", "You earned a total of");
        if(HudPixelConfig.blitzCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.blitzDeathmatchNotifier) {
            this.components.add(new BlitzDeathmatchNotifier());
        }
        
    }

}
