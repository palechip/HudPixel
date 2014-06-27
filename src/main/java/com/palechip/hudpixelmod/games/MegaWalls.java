package com.palechip.hudpixelmod.games;

import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.MegaWallsKillCounter;
import com.palechip.hudpixelmod.components.MegaWallsKillCounter.KillType;

public class MegaWalls extends Game {

    public MegaWalls() {
        // don't start the game before the walls are down
        // it's not necessary and it prevents the game from breaking when
        // the player relogs during this time.
        super("Mega Walls", "Mega Walls", "The walls have come down! Prepare for battle!", "You earned a total of");
        this.components.add(new CoinCounterComponent());
        this.components.add(new MegaWallsKillCounter(KillType.Normal));
        this.components.add(new MegaWallsKillCounter(KillType.Final));
    }
}
