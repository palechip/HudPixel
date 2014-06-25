package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;

public class VampireZ extends Game {
    public VampireZ() {
        super("VampireZ", "", "30 seconds till the first wave, get ready!", "You earned a total of");
        if(HudPixelConfig.vampireTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.vampireCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }
}
