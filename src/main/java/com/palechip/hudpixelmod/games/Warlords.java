package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

import net.hypixel.api.util.GameType;

public class Warlords extends Game {

    public Warlords() {
        super("Warlords", "Warlords", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, null, HudPixelConfig.WARLORDS_CATEGORY);
        if(HudPixelConfig.warlordsCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
