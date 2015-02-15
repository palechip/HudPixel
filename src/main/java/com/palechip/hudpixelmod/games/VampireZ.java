package com.palechip.hudpixelmod.games;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.components.VampireZBalanceComponent;
import com.palechip.hudpixelmod.components.VampireZBalanceComponent.Type;

public class VampireZ extends Game {
    public VampireZ() {
        super("VampireZ", "", "30 seconds till the first wave, get ready!", END_MESSAGE_DEFAULT, GameType.VAMPIREZ, HudPixelConfig.VAMPIREZ_CATEGORY);
        if(HudPixelConfig.vampireTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.vampireCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.vampireBalance) {
            this.components.add(new VampireZBalanceComponent(Type.Negative));
            this.components.add(new VampireZBalanceComponent(Type.Total));
        }
    }
}
