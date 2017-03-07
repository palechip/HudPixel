/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package com.palechip.hudpixelmod.extended.util;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.ResourceLocation;

public class ImageLoader {

    private final static String BASE_PATH = "textures/gameicons/";
    private final static String EXTENSION = "-64.png";
    public static final ResourceLocation INVENTORY_RES = new ResourceLocation("textures/gui/container/inventory.png");

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

    public static ResourceLocation shadowLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "textures/msc/SHADOW_TEST.png");
    }

    public static ResourceLocation chatMaximizeLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "icons/chatGUIicons-01.png");
    }
    public static ResourceLocation chatMinimizeLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "icons/chatGUIicons-02.png");
    }
    public static ResourceLocation chatTabAddLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "icons/chatGUIicons-03.png");
    }
    public static ResourceLocation chatMoveLocation() {
        return new ResourceLocation(HudPixelMod.MODID, "icons/chatGUIicons-04.png");
    }


}
