/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 * <p>
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
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
package com.palechip.hudpixelmod.util;

/**
 * Replaces GameType and only holds the modID. It can be used to statically reference a certain game without storing any information which may be changed.
 * @author palechip
 * //Added HungerGames - hst
 */
public enum GameType {
    UNKNOWN(-1, "UNKNOWN", "", "none"),
    ALL_GAMES(0, "ALL GAMES", "", "any"),
    QUAKECRAFT(1, "Quakecraft", "quakecraft", "QUAKECRAFT"),
    THE_WALLS(2, "Walls", "walls", "THE WALLS"),
    PAINTBALL(3, "Paintball", "paintball", "PAINTBALL"),
    BLITZ(4, "Blitz Survival Games", "blitz", "BLITZ SG"),
    TNT_GAMES(5, "TNT Games", "tnt", "THE TNT GAMES"),
    BOW_SPLEEF(6, "Bow Spleef", "", TNT_GAMES.scoreboardName),
    TNT_RUN(7, "TNT Run", "", TNT_GAMES.scoreboardName),
    TNT_WIZARDS(8, "TNT Wizards", "", TNT_GAMES.scoreboardName),
    TNT_TAG(9, "TNT Tag", "", TNT_GAMES.scoreboardName),
    ANY_TNT(5, TNT_GAMES.name, TNT_GAMES.tipName, TNT_GAMES.scoreboardName),
    VAMPIREZ(10, "VampireZ", "vampirez", "VAMPIREZ"),
    MEGA_WALLS(11, "Mega Walls", "mega", "MEGA WALLS"),
    ARENA(12, "Arena Brawl", "arena", "ARENA BRAWL"),
    UHC(13, "UHC Champions", "uhc", "UHC CHAMPIONS"),
    COPS_AND_CRIMS(14, "Cops and Crims", "cops", "COPS AND CRIMS"),
    WARLORDS(15, "Warlords", "warlords", "WARLORDS"),
    ARCADE_GAMES(16, "Arcade Games", "arcade", "ARCADE GAMES"),
    BLOCKING_DEAD(17, "Blocking Dead", "", ARCADE_GAMES.scoreboardName),
    BOUNTY_HUNTERS(18, "Bounty Hunters", "", ARCADE_GAMES.scoreboardName),
    BUILD_BATTLE(19, "Build Battle", "", ARCADE_GAMES.scoreboardName),
    CREEPER_ATTACK(20, "Creeper Attack", "", ARCADE_GAMES.scoreboardName),
    DRAGON_WARS(21, "Dragon Wars", "", ARCADE_GAMES.scoreboardName),
    ENDER_SPLEEF(22, "Ender Spleef", "", ARCADE_GAMES.scoreboardName),
    FARM_HUNT(23, "Farm Hunters", "", ARCADE_GAMES.scoreboardName),
    GALAXY_WARS(24, "Galaxy Wars", "", ARCADE_GAMES.scoreboardName),
    PARTY_GAMES_1(25, "Party Games", "", ARCADE_GAMES.scoreboardName),
    PARTY_GAMES_2(26, "Party Games", "", ARCADE_GAMES.scoreboardName),
    TRHOW_OUT(27, "Throw Out", "", ARCADE_GAMES.scoreboardName),
    TURBO_KART_RACERS(28, "Turbo Kart Racers", "turbo", "TURBO KART RACERS"),
    SPEED_UHC(29, "Speed UHC", "speed", "SPEED UHC"),
    CRAZY_WALLS(31, "Crazy Walls", "crazy", "CRAZY WALLS"),
    SMASH_HEROES(32, "Smash Heroes", "smash", " SMASH HEROES"),
    SMASH_HEROES_WOSPACE(32, "Smash Heroes", "smash", "SMASH HEROES"),
    SKYWARS(30, "SkyWars", "skywars", "SKYWARS"),
    FOOTBALL(33, "Football", "", ARCADE_GAMES.scoreboardName),
    ANY_ARCADE(16, ARCADE_GAMES.name, ARCADE_GAMES.tipName, ARCADE_GAMES.scoreboardName);
    private final int modID;
    private final String name;
    private final String tipName;
    public final String scoreboardName;

    public String getName() {
        return name;
    }

    public String getTipName() {
        return tipName;
    }

    GameType(int modID, String name, String tipName, String scoreboardName) {
        this.tipName = tipName;
        this.modID = modID;
        this.name = name;
        this.scoreboardName = scoreboardName;
    }


    public int getModID() {
        return modID;
    }

    public static GameType getTypeByName(String name) {
        for (GameType type : GameType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return GameType.UNKNOWN;
    }

    public static GameType getTypeByID(int modid) {
        for (GameType type : GameType.values()) {
            if (type.modID == modid) {
                return type;
            }
        }
        return GameType.UNKNOWN;
    }
}
