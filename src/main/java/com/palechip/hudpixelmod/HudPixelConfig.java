package com.palechip.hudpixelmod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class HudPixelConfig {
    private Configuration config;
    
    public static final String QUAKE_CATEGORY = "Quake";
    public static final String TNT_CATEGORY = "TNT Games";
    public static final String VAMPIREZ_CATEGORY = "VampireZ";
    public static final String ARCADE_CATEGORY = "Arcade Games";
    public static final String WALLS_CATEGORY = "The Walls";
    public static final String MEGA_WALLS_CATEGORY = "Mega Walls";
    public static final String BLITZ_CATEGORY = "Blitz Survival Games";
    public static final String ARENA_CATEGORY = "Arena Brawl";
    public static final String PAINTBALL_CATEGORY = "Paintball Warfare";
    // define further categories here
    
    public static boolean quakeCoinDisplay;
    public static boolean quakeTimeDisplay;
    public static boolean tntCoinDisplay;
    public static boolean tntTimeDisplay;
    public static boolean vampireCoinDisplay;
    public static boolean vampireTimeDisplay;
    public static boolean arcadeCoinDisplay;
    public static boolean arcadeTimeDisplay;
    public static boolean wallsCoinDisplay;
    public static boolean wallsTimeDisplay;
    public static boolean megaWallsCoinDisplay;
    public static boolean blitzCoinDisplay;
    public static boolean arenaCoinDisplay;
    public static boolean arenaPowerupDisplay;
    public static boolean paintballTimeDisplay;
    public static boolean paintballCoinDisplay;
    public static boolean paintballKillstreakTimerDisplay;
    // add further options here

    public HudPixelConfig(File config) {
        this.config = new Configuration(config);
    }
    
    public void loadConfig() {
        this.config.load();
        quakeCoinDisplay = this.config.get(QUAKE_CATEGORY, "coinDisplay", true).getBoolean(true);
        quakeTimeDisplay = this.config.get(QUAKE_CATEGORY, "timeDisplay", true).getBoolean(true);
        tntCoinDisplay = this.config.get(TNT_CATEGORY, "coinDisplay", true).getBoolean(true);
        tntTimeDisplay = this.config.get(TNT_CATEGORY, "timeDisplay", true).getBoolean(true);
        vampireCoinDisplay = this.config.get(VAMPIREZ_CATEGORY, "coinDisplay", true).getBoolean(true);
        vampireTimeDisplay = this.config.get(VAMPIREZ_CATEGORY, "timeDisplay", true).getBoolean(true);
        arcadeCoinDisplay = this.config.get(ARCADE_CATEGORY, "coinDisplay", true).getBoolean(true);
        arcadeTimeDisplay = this.config.get(ARCADE_CATEGORY, "timeDisplay", true).getBoolean(true);
        wallsCoinDisplay = this.config.get(WALLS_CATEGORY, "coinDisplay", true).getBoolean(true);
        wallsTimeDisplay = this.config.get(WALLS_CATEGORY, "timeDisplay", true).getBoolean(true);
        megaWallsCoinDisplay = this.config.get(MEGA_WALLS_CATEGORY, "coinDisplay", true).getBoolean(true);
        blitzCoinDisplay = this.config.get(BLITZ_CATEGORY, "coinDisplay", true).getBoolean(true);
        arenaCoinDisplay = this.config.get(ARENA_CATEGORY, "coinDisplay", true).getBoolean(true);
        arenaPowerupDisplay = this.config.get(ARENA_CATEGORY, "PowerupDisplay", true).getBoolean(true);
        paintballCoinDisplay = this.config.get(PAINTBALL_CATEGORY, "coinDisplay", true).getBoolean(true);
        paintballTimeDisplay = this.config.get(PAINTBALL_CATEGORY, "timeDisplay", true).getBoolean(true);
        paintballKillstreakTimerDisplay = this.config.get(PAINTBALL_CATEGORY, "killstreakTimerDisplay", true).getBoolean(true);
        // load further options here
        this.config.save();
    }
}
