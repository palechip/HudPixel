package com.palechip.hudpixelmod.games.tnt;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class BowSpleef extends Game {
    public BowSpleef() {
        super("Bow Spleef", "", "The game has now begun!", END_MESSAGE_DEFAULT, GameType.TNTGAMES);
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }
}
