package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;

import net.hypixel.api.util.GameType;

public class Warlords extends Game {

    public Warlords() {
        super(null, "WARLORDS", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.BATTLEGROUND , HudPixelConfig.WARLORDS_CATEGORY);
        if(HudPixelConfig.warlordsTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.warlordsCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
