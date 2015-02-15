package com.palechip.hudpixelmod.games.tnt;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.KillstreakTracker;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class Wizards extends Game {
    public Wizards() {
        super("TNT Wizards", "", "You are on team", END_MESSAGE_DEFAULT, GameType.TNTGAMES, HudPixelConfig.TNT_CATEGORY);
        if(HudPixelConfig.tntTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.tntCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.tntKillstreakTracker) {
            this.components.add(new KillstreakTracker());
        }
    }
}
