package com.palechip.hudpixelmod.games;

import net.hypixel.api.util.GameType;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;
import com.palechip.hudpixelmod.components.PaintballKillstreakTracker;
import com.palechip.hudpixelmod.components.TimerComponent;

public class Paintball extends Game {

    protected Paintball() {
        super("PB", "Paintball", START_MESSAGE_DEFAULT, END_MESSAGE_DEFAULT, GameType.PAINTBALL, HudPixelConfig.PAINTBALL_CATEGORY);
        if(HudPixelConfig.paintballTimeDisplay) {
            this.components.add(new TimerComponent());
        }
        if(HudPixelConfig.paintballCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.paintballKillstreakTrackerDisplay) {
            // You can't use Bomberman when Lightning is on cooldown and vice versa
            PaintballKillstreakTracker lightning = new PaintballKillstreakTracker("Lightning", false, true);
            PaintballKillstreakTracker bomberman = new PaintballKillstreakTracker("Bomberman", false, true);
            PaintballKillstreakTracker endereye = new PaintballKillstreakTracker("Ender Eye", false, true);
            PaintballKillstreakTracker enderpearl = new PaintballKillstreakTracker("Ender Pearl", false, true);
            
            lightning.addCooldownDependantKillstreak(bomberman);
            bomberman.addCooldownDependantKillstreak(lightning);
            
            endereye.addCooldownDependantKillstreak(enderpearl);
            enderpearl.addCooldownDependantKillstreak(endereye);
            
            // Triple Shot
            this.components.add(new PaintballKillstreakTracker("Triple Shot", true, true));
            // Quintuple Shot
            this.components.add(new PaintballKillstreakTracker("Quintuple Shot", true, false));
            // Strong Arm
            this.components.add(new PaintballKillstreakTracker("Strong Arm", true, true));
            // Forcefield
            this.components.add(new PaintballKillstreakTracker("Force Field", true, true));
            // Super Strong Arm
            this.components.add(new PaintballKillstreakTracker("Super Strong Arm", true, true));
            // Rambo
            this.components.add(new PaintballKillstreakTracker("RAMBO", true, true));
            // Backstab
            this.components.add(new PaintballKillstreakTracker("Backstab", true, false));
            // Sentry
            this.components.add(new PaintballKillstreakTracker("Sentry", true, true));
            // cooldown only killsteaks:
            // TNT Rain
            this.components.add(new PaintballKillstreakTracker("TNT Rain", false, true));
            // Nuke
            this.components.add(new PaintballKillstreakTracker("Nuke", false, true));
            //ender eye
            this.components.add(endereye);
            //ender pearl
            this.components.add(enderpearl);
            // Lightning
            this.components.add(lightning);
            // Bomberman
            this.components.add(bomberman);
        }
    }

}
