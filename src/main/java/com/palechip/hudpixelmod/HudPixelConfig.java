package com.palechip.hudpixelmod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class HudPixelConfig {
    private Configuration config;

    // categories
    // when adding a new category, don't forget to add it to HudPixelConfigGui
    public static final String DISPLAY_CATEGORY = "display";
    public static final String QUAKE_CATEGORY = "quake";
    public static final String TNT_CATEGORY = "tnt games";
    public static final String VAMPIREZ_CATEGORY = "vampirez";
    public static final String ARCADE_CATEGORY = "arcade games";
    public static final String WALLS_CATEGORY = "the walls";
    public static final String MEGA_WALLS_CATEGORY = "mega walls";
    public static final String BLITZ_CATEGORY = "blitz survival games";
    public static final String ARENA_CATEGORY = "arena brawl";
    public static final String PAINTBALL_CATEGORY = "paintball warfare";
    // define further categories here

    // properties
    public static String displayMode;
    public static int displayXOffset;
    public static int displayYOffset;
    public static int displayShowResultTime;
    public static boolean displayVersion;
    public static boolean quakeCoinDisplay;
    public static boolean quakeTimeDisplay;
    public static boolean quakeKillstreakTracker;
    public static boolean tntCoinDisplay;
    public static boolean tntTimeDisplay;
    public static boolean tntKillstreakTracker;
    public static boolean vampireCoinDisplay;
    public static boolean vampireTimeDisplay;
    public static boolean arcadeCoinDisplay;
    public static boolean arcadeTimeDisplay;
    public static boolean arcadeKillstreakTracker;
    public static boolean wallsCoinDisplay;
    public static boolean wallsTimeDisplay;
    public static boolean megaWallsCoinDisplay;
    public static boolean megaWallsKillCounter;
    public static boolean blitzCoinDisplay;
    public static boolean blitzDeathmatchNotifier;
    public static boolean arenaCoinDisplay;
    public static boolean paintballTimeDisplay;
    public static boolean paintballCoinDisplay;
    public static boolean paintballKillstreakTrackerDisplay;
    // add further options here

    // descriptions
    public static final String COIN_COUNTER = "Turn on/off the Coin Counter for ";
    public static final String TIMER = "Turn on/off the Time Display for ";
    public static final String MEGA_WALLS_KILL_COUNTER = "Enable/Disable the Kill and Assist Counter in Mega Walls";
    public static final String PAINTBALL_KILLSTREAK_TRACKER = "Show/Hide your active Paintball killstreaks and the ones on cooldown.";
    public static final String KILLSTREAK_TRACKER = "Enable/Disable tracking how many kills you can get within one live in ";
    public static final String BLITZ_DEATHMATCH_NOTIFIER = "Show/Hide a little flashing message when death match is near in Blitz Survival Games";

    public HudPixelConfig(File config) {
        this.config = new Configuration(config);
        // if this is an old config file, the version is null
        // also happens when the config is generated but this.updateConfig() won't hurt
        if(this.config.getLoadedConfigVersion() == null) {
            this.updateConfig();
            this.config = new Configuration(config, "2");
        }
        this.config.load();
    }

    public void syncConfig() {
        displayMode = this.config.get(DISPLAY_CATEGORY, "displayMode", "lefttop", "Choose where to render everything the mod displays.(\"lefttop\", \"righttop\", \"leftbottom\" and \"rightbottom\")").getString();
        displayXOffset = this.config.get(DISPLAY_CATEGORY, "xOffset", 0, "This value will be added to the X (horizontal) position before rendering.").getInt();
        displayYOffset = this.config.get(DISPLAY_CATEGORY, "yOffset", 0, "This value will be added to the Y (vertical) position before rendering.").getInt();
        displayShowResultTime = this.config.get(DISPLAY_CATEGORY, "showResultTime", 20, "How long (in seconds) the results will be shown after a game. Use -1 so it stays until the next game starts.").getInt(20);
        displayVersion = this.config.get(DISPLAY_CATEGORY, "displayVersion", true, "Show the mod version and name when there is nothing else to show.").getBoolean();
        quakeCoinDisplay = this.config.get(QUAKE_CATEGORY, "quakeCoinDisplay", true ,COIN_COUNTER + "Quakecraft.").getBoolean(true);
        quakeTimeDisplay = this.config.get(QUAKE_CATEGORY, "quakeTimeDisplay", true, TIMER + "Quakecraft.").getBoolean(true);
        quakeKillstreakTracker = this.config.get(QUAKE_CATEGORY, "quakeKillstreakTracker", true, KILLSTREAK_TRACKER + "Quakecraft.").getBoolean(true);
        tntCoinDisplay = this.config.get(TNT_CATEGORY, "tntCoinDisplay", true,COIN_COUNTER + "the TNT Games").getBoolean(true);
        tntTimeDisplay = this.config.get(TNT_CATEGORY, "tntTimeDisplay", true, TIMER + "the TNT Games").getBoolean(true);
        tntKillstreakTracker = this.config.get(TNT_CATEGORY, "tntKillstreakTracker", true, KILLSTREAK_TRACKER + "TNT Wizards.").getBoolean(true);
        vampireCoinDisplay = this.config.get(VAMPIREZ_CATEGORY, "vampireZCoinDisplay", true, COIN_COUNTER + "VampireZ.").getBoolean(true);
        vampireTimeDisplay = this.config.get(VAMPIREZ_CATEGORY, "vampireZTimeDisplay", true, TIMER + "VampireZ.").getBoolean(true);
        arcadeCoinDisplay = this.config.get(ARCADE_CATEGORY, "arcadeCoinDisplay", true, COIN_COUNTER + "the Arcade Games.").getBoolean(true);
        arcadeTimeDisplay = this.config.get(ARCADE_CATEGORY, "acrcadeTimeDisplay", true, TIMER + "the Arcade Games.").getBoolean(true);
        arcadeKillstreakTracker = this.config.get(ARCADE_CATEGORY, "arcadeKillstreakTracker", true, KILLSTREAK_TRACKER + "some Arcade Games.").getBoolean(true);
        wallsCoinDisplay = this.config.get(WALLS_CATEGORY, "wallsCoinDisplay", true, COIN_COUNTER + "the classic Walls.").getBoolean(true);
        wallsTimeDisplay = this.config.get(WALLS_CATEGORY, "wallsTimeDisplay", true, TIMER + "the classic Walls.").getBoolean(true);
        megaWallsCoinDisplay = this.config.get(MEGA_WALLS_CATEGORY, "megaWallsCoinDisplay", true, COIN_COUNTER + "Mega Walls.").getBoolean(true);
        megaWallsKillCounter = this.config.get(MEGA_WALLS_CATEGORY, "megaWallsKillCounter", true, MEGA_WALLS_KILL_COUNTER).getBoolean(true);
        blitzCoinDisplay = this.config.get(BLITZ_CATEGORY, "blitzCoinDisplay", true, COIN_COUNTER + "Blitz Survival Games.").getBoolean(true);
        blitzDeathmatchNotifier = this.config.get(BLITZ_CATEGORY, "blitzDeathmatchNotifier", true, BLITZ_DEATHMATCH_NOTIFIER).getBoolean(true);
        arenaCoinDisplay = this.config.get(ARENA_CATEGORY, "arenaCoinDisplay", true, COIN_COUNTER + "Arena Brawl.").getBoolean(true);
        paintballCoinDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballCoinDisplay", true, COIN_COUNTER + "Paintball Warfare.").getBoolean(true);
        paintballTimeDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballTimeDisplay", true, TIMER + "Paintball Warfare.").getBoolean(true);
        paintballKillstreakTrackerDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballKillstreakTrackerDisplay", true, PAINTBALL_KILLSTREAK_TRACKER).getBoolean(true);
        // load further options here

        if(this.config.hasChanged()) {
            this.config.save();
        }
    }

    public Configuration getConfigFile() {
        return config;
    }

    // for the config gui introduced in 2.1.0, most properties need to be renamed
    private void updateConfig() {
        this.config.load();
        this.renameBooleanProperty(QUAKE_CATEGORY, "coinDisplay", "quakeCoinDisplay");
        this.renameBooleanProperty(QUAKE_CATEGORY, "timeDisplay", "quakeTimeDisplay");
        this.renameBooleanProperty(TNT_CATEGORY, "coinDisplay", "tntCoinDisplay");
        this.renameBooleanProperty(TNT_CATEGORY, "timeDisplay", "tntTimeDisplay");
        this.renameBooleanProperty(VAMPIREZ_CATEGORY, "coinDisplay", "vampireZTimeDisplay");
        this.renameBooleanProperty(VAMPIREZ_CATEGORY, "timeDisplay", "vampireZTimeDisplay");
        this.renameBooleanProperty(ARCADE_CATEGORY, "coinDisplay", "arcadeCoinDisplay");
        this.renameBooleanProperty(ARCADE_CATEGORY, "timeDisplay", "acrcadeTimeDisplay");
        this.renameBooleanProperty(WALLS_CATEGORY, "coinDisplay", "wallsCoinDisplay");
        this.renameBooleanProperty(WALLS_CATEGORY, "timeDisplay", "wallsTimeDisplay");
        this.renameBooleanProperty(MEGA_WALLS_CATEGORY, "coinDisplay", "megaWallsCoinDisplay");
        this.renameBooleanProperty(MEGA_WALLS_CATEGORY, "killCounter", "megaWallsKillCounter");
        this.renameBooleanProperty(BLITZ_CATEGORY, "coinDisplay", "blitzCoinDisplay");
        // the deathmatch notifier wasn't included in the config for 2.0.0 as it was disabled
        this.renameBooleanProperty(ARENA_CATEGORY, "coinDisplay", "arenaCoinDisplay");
        this.renameBooleanProperty(PAINTBALL_CATEGORY, "coinDisplay", "paintballCoinDisplay");
        this.renameBooleanProperty(PAINTBALL_CATEGORY, "timeDisplay", "paintballTimeDisplay");
        // the paintball killstreak tracker wasn't included in the config for 2.0.0 as it was disabled
        this.config.save();
    }

    /**
     * Renames a property in a given category. But other than Configuration.renameProperty(), it does turn it into a boolean property.
     * 
     * @param category the category in which the property resides
     * @param oldPropName the existing property name
     * @param newPropName the new property name
     * @return true if the category and property exist, false otherwise
     */
    public boolean renameBooleanProperty(String category, String oldPropName, String newPropName)
    {
        if (this.config.hasCategory(category))
        {
            if (this.config.getCategory(category).containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName))
            {
                this.config.get(category, newPropName, this.config.getCategory(category).get(oldPropName).getBoolean());
                this.config.getCategory(category).remove(oldPropName);
                return true;
            }
        }
        return false;
    }
}
