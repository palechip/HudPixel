/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.extended.util;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.ResourceLocation;

public class ImageLoader {

    private final static String BASE_PATH = "textures/gameicons/";
    private final static String EXTENSION = "-64.png";

    /**
     * Add a new icon for a gametype here ;)
     *
     * @param gameType GameType the icon should be loaded for
     * @return resourcelocation of the game type.
     */
    public static ResourceLocation gameIconLocation(GameType gameType) {
        switch (gameType) {
            case QUAKECRAFT:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Quakecraft" + EXTENSION);
            case THE_WALLS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Walls" + EXTENSION);
            case PAINTBALL:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Paintball" + EXTENSION);
            case BLITZ:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "SG" + EXTENSION);
            case TNT_GAMES:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "TNT" + EXTENSION);
            case VAMPIREZ:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "VampireZ" + EXTENSION);
            case MEGA_WALLS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "MegaWalls" + EXTENSION);
            case ARENA:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Arena" + EXTENSION);
            case UHC:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "UHC" + EXTENSION);
            case COPS_AND_CRIMS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "CVC" + EXTENSION);
            case WARLORDS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Warlords" + EXTENSION);
            case ARCADE_GAMES:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Arcade" + EXTENSION);
            case SPEED_UHC:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "SpeedUHC" + EXTENSION);
            case SMASH_HEROES:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "SmashHeroes" + EXTENSION);
            case CRAZY_WALLS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "CrazyWalls" + EXTENSION);
            case SKYWARS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "Skywars" + EXTENSION);
            case TURBO_KART_RACERS:
                return new ResourceLocation(HudPixelMod.MODID, BASE_PATH + "TurboKartRacers" + EXTENSION);
            default:
                return null;
        }
    }

    //TODO: make this work with enums for better futureproof
    public static ResourceLocation partyLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "textures/msc/Party.png");
    }

    public static ResourceLocation boosterTip() {
        return new ResourceLocation(HudPixelMod.MODID, "textures/msc/BoosterTip.png");
    }

    public static ResourceLocation chatLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "textures/msc/Chat.png");
    }
}
