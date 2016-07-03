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
    UNKNOWN         (-1, "UNKNOWN"),
    ALL_GAMES       (0,  "ALL GAMES"),
    QUAKECRAFT      (1,  "Quakecraft"),
    THE_WALLS       (2,  "Walls"),
    PAINTBALL       (3,  "Paintball"),
    BLITZ           (4,  "Blitz Survival Games" ),
    TNT_GAMES       (5,  "TNT Games"),
    BOW_SPLEEF      (6,  "Bow Spleef"),
    TNT_RUN         (7,  "TNT Run"),
    TNT_WIZARDS     (8,  "TNT Wizards"),
    TNT_TAG         (9,  "TNT Tag"),
    VAMPIREZ        (10, "VampireZ"),
    MEGA_WALLS      (11, "Mega Walls"),
    ARENA           (12, "Arena Brawl"),
    UHC             (13, "UHC Champions"),
    COPS_AND_CRIMS  (14, "Cops and Crimes"),
    WARLORDS        (15, "Warlords"),
    ARCADE_GAMES    (16, "Arcade Games"),
    BLOCKING_DEAD   (17, "Blocking Dead"),
    BOUNTY_HUNTERS  (18, "Bounty Hunters"),
    BUILD_BATTLE    (19, "Build Battle"),
    CREEPER_ATTACK  (20, "Creeper Attack"),
    DRAGON_WARS     (21, "Dragon Wars"),
    ENDER_SPLEEF    (22, "Ender Spleef"),
    FARM_HUNT       (23, "Farm Hunters"),
    GALAXY_WARS     (24, "Galaxy Wars"),
    PARTY_GAMES_1   (25, "Party Games"),
    PARTY_GAGMES_2  (26, "Party Games"),
    TRHOW_OUT       (27, "Throw Out"),
    TURBO_KART_RACERS(28,"Turbo Kart Racers"),

    //TODO: ADD THIS TO THE CONFIG
    SPEED_UHC       (29, "Speed UHC"),
    CRAZY_WALLS     (31, "Crazy Walls"),
    SMASH_HEROES    (32, "Smash Heroes"),

    SKYWARS         (30, "SkyWars"),
    FOOTBALL        (33, "Football");

    private final int modID;
    private final String name;

    public String getName() {
        return name;
    }

    GameType(int modID, String name) {

        this.modID = modID;
        this.name = name;
    }


    public int getModID() {
        return modID;
    }

    public static GameType getTypeByName(String name) {
        for(GameType type : GameType.values()) {
            if(type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return GameType.UNKNOWN;
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
