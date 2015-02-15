package com.palechip.hudpixelmod.games;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Arena extends Game {

    public Arena() {
        super("Arena", "", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.ARENA, HudPixelConfig.ARENA_CATEGORY);
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
