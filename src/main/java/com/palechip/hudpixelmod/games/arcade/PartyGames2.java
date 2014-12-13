package com.palechip.hudpixelmod.games.arcade;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class PartyGames2 extends Game {

    public PartyGames2() {
        super("", "Party Games 2", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.ARCADE);
        if(HudPixelConfig.arcadeTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }

}
