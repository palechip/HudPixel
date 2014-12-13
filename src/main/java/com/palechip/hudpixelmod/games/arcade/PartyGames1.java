package com.palechip.hudpixelmod.games.arcade;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.TimerComponent;
import com.palechip.hudpixelmod.games.Game;

public class PartyGames1 extends Game {

    public PartyGames1() {
        super("", "Party Games 1", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.ARCADE);
        if(HudPixelConfig.arcadeTimeDisplay) {
            this.components.add(new TimerComponent());
        }
    }

}
