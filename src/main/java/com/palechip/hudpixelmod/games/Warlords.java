package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.components.WarlordsDamageAndHealingCounter;
import com.palechip.hudpixelmod.components.WarlordsDamageAndHealingCounter.Type;

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
        if(HudPixelConfig.warlordsDamageAndHealthCounter) {
            this.components.add(new WarlordsDamageAndHealingCounter(Type.Damage));
            this.components.add(new WarlordsDamageAndHealingCounter(Type.Healing));
        }
    }

}
