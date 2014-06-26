package com.palechip.hudpixelmod.games;

import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.ArenaPowerupHighlighter;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Arena extends Game {

    public Arena() {
        super("Arena", "", "The game starts in 1 seconds", "You earned a total of");
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
        if(HudPixelConfig.arenaPowerupDisplay) {
            this.components.add(new ArenaPowerupHighlighter("HEALING", EnumChatFormatting.GREEN));
            this.components.add(new ArenaPowerupHighlighter("DOUBLE DAMAGE", EnumChatFormatting.RED));
            this.components.add(new ArenaPowerupHighlighter("MAGICAL KEY", EnumChatFormatting.AQUA));
        }
        
    }

}
