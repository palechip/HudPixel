package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.util.GameType;

import java.util.ArrayList;

public class CooldownManagerFactory {

    public static ArrayList<CooldownDisplayModule> setCooldownDisplay(GameType gameType) {
        ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
        if (gameType == GameType.WARLORDS) {
            cdModules.add(new CooldownDisplayModule(351, 1, 1));
            cdModules.add(new CooldownDisplayModule(348, 0, 2));
            cdModules.add(new CooldownDisplayModule(351, 10, 3));
            cdModules.add(new CooldownDisplayModule(351, 14, 4));
        }
        return cdModules;
    }
}
