/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.util;

/**
 * Replaces GameType and only holds the modID. It can be used to statically reference a certain game without storing any information which may be changed.
 * @author palechip
 *
 */
public enum GameType {
    UNKNOWN(-1),
    ALL_GAMES(0),
    QUAKECRAFT(1),
    THE_WALLS(2),
    PAINTBALL(3),
    BLITZ(4),
    TNT_GAMES(5),
    BOW_SPLEEF(6),
    TNT_RUN(7),
    TNT_WIZARDS(8),
    TNT_TAG(9),
    VAMPIREZ(10),
    MEGA_WALLS(11),
    ARENA(12),
    UHC(13),
    COPS_AND_CRIMS(14),
    WARLORDS(15),
    ARCADE_GAMES(16),
    BLOCKING_DEAD(17),
    BOUNTY_HUNTERS(18),
    BUILD_BATTLE(19),
    CREEPER_ATTACK(20),
    DRAGON_WARS(21),
    ENDER_SPLEEF(22),
    FARM_HUNT(23),
    GALAXY_WARS(24),
    PARTY_GAMES_1(25),
    PARTY_GAGMES_2(26),
    TRHOW_OUT(27),
    TURBO_KART_RACERS(28);

    private final int modID;

    private GameType(int modID) {
        this.modID = modID;
    }


    public int getModID() {
        return modID;
    }

    public static GameType getTypeByID(int modid) {
        for(GameType type : GameType.values()) {
            if(type.modID == modid) {
                return type;
            }
        }
        return GameType.UNKNOWN;
    }
}
