package com.palechip.hudpixelmod.games.tnt;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Tag extends Game {
    public Tag() {
        super("TNT Tag", "", "The TNT has been released!", END_MESSAGE_DEFAULT, GameType.TNTGAMES, HudPixelConfig.TNT_CATEGORY);
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.tntCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }
}
