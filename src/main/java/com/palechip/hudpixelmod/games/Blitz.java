package com.palechip.hudpixelmod.games;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.BlitzDeathmatchNotifier;
import com.palechip.hudpixelmod.components.BlitzStarTracker;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Blitz extends Game {

    public Blitz() {
        super("B-SG", "Blitz SG", "You will get the items for your", END_MESSAGE_DEFAULT, GameType.SURVIVAL_GAMES, HudPixelConfig.BLITZ_CATEGORY);
        if(HudPixelConfig.blitzCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.blitzStarTracker) {
            this.components.add(new BlitzStarTracker());
        }
        if(HudPixelConfig.blitzDeathmatchNotifier) {
            this.components.add(new BlitzDeathmatchNotifier());
        }
        
    }

}
