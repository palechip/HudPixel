package com.palechip.hudpixelmod.games;

import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.components.CoinCounterComponent;

public class Arena extends Game {

    public Arena() {
        super("Arena", "", "The game starts in 1 second!", "You earned a total of");
        if(HudPixelConfig.arcadeCoinDisplay) {
            this.components.add(new CoinCounterComponent());
        }
    }

}
