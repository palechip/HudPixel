package com.palechip.hudpixelmod.games.arcade;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.KillstreakTracker;
import com.palechip.hudpixelmod.games.Game;

public class DragonWars extends Game {

    public DragonWars() {
        super("", "Dragon Wars", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.ARCADE);
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.arcadeKillstreakTracker) {
            this.components.add(new KillstreakTracker());
        }
    }
}
