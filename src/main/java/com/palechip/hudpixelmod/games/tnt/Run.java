package com.palechip.hudpixelmod.games.tnt;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Run extends Game {
    public Run() {
        super("TNT Run", "", "Go!", END_MESSAGE_DEFAULT, GameType.TNTGAMES);
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }
}
