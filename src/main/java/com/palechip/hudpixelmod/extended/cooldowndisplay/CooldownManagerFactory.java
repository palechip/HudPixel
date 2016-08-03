package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.extended.util.ImageLoader;
import com.palechip.hudpixelmod.util.GameType;

import java.util.ArrayList;

public class CooldownManagerFactory {

    public static ArrayList<CooldownDisplayModule> setCooldownDisplay(GameType gameType){
        ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
        if(gameType == GameType.WARLORDS){
            cdModules.add(new CooldownDisplayModule(ImageLoader.gameIconLocation(GameType.WARLORDS), 2));
            //add more here :D
        }
        return cdModules;
    }
}
