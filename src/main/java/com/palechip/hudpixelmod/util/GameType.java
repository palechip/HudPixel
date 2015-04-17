package com.palechip.hudpixelmod.util;

/**
 * Replaces net.hypixel.api.util.GameType and only holds the modID. It can be used to statically reference a certain game without storing any information which may be changed.
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
    TRHOW_OUT(27);

    private final int modID;

    private GameType(int modID) {
        this.modID = modID;
    }


    public int getModID() {
        return modID;
    }

    public GameType getTypeByID(int modid) {
        for(GameType type : GameType.values()) {
            if(type.modID == modid) {
                return type;
            }
        }
        return GameType.UNKNOWN;
    }
}
