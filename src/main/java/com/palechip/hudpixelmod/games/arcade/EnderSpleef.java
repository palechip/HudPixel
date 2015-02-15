package com.palechip.hudpixelmod.games.arcade;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class EnderSpleef extends Game {

    public EnderSpleef() {
        super("", "Ender Spleef", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.ARCADE, HudPixelConfig.ARCADE_CATEGORY);
        if(HudPixelConfig.arcadeTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }

}
