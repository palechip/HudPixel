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
    public static final String COPS_AND_CRIMS_CATEGORY = "cops and crims";
    public static final String UHC_CATEGORY = "uhc";
    public static final String WARLORDS_CATEGORY = "warlords";
    // define further categories here

    // properties
    public static boolean useAPI;
    public static boolean autoCompleteSecondLobbyCmd;
    public static int filterCoins;
    public static String displayMode;
    public static int displayXOffset;
    public static int displayYOffset;
    public static int displayShowResultTime;
    public static boolean displayVersion;
    public static boolean displayNetworkBoosters;
    public static boolean displayTipAllButton;
    public static boolean quakeCoinDisplay;
    public static boolean quakeTimeDisplay;
    public static boolean quakeKillstreakTracker;
    public static boolean tntCoinDisplay;
    public static boolean tntTimeDisplay;
    public static boolean tntKillstreakTracker;
    public static boolean vampireCoinDisplay;
    public static boolean vampireTimeDisplay;
    public static boolean vampireBalance;
    public static boolean arcadeCoinDisplay;
    public static boolean arcadeTimeDisplay;
    public static boolean arcadeKillstreakTracker;
    public static boolean wallsCoinDisplay;
    public static boolean wallsTimeDisplay;
    public static boolean wallsKillCounter;
    public static boolean megaWallsCoinDisplay;
    public static boolean megaWallsWitherCoinDisplay;
    public static boolean megaWallsKillCounter;
    public static boolean blitzCoinDisplay;
    public static boolean blitzDeathmatchNotifier;
    public static boolean blitzStarTracker;
    public static boolean arenaCoinDisplay;
    public static boolean paintballTimeDisplay;
    public static boolean paintballCoinDisplay;
    public static boolean paintballKillstreakTrackerDisplay;
    public static boolean copsAndCrimsCoinDisplay;
    public static boolean copsAndCrimsTimeDisplay;
    public static boolean uhcCoinDisplay;
    public static boolean warlordsCoinDisplay;
    public static boolean warlordsTimeDisplay;
    public static boolean warlordsDamageAndHealthCounter;
    public static int warlordsFilterDamageDone;
    public static int warlordsFilterDamageTaken;
    public static int warlordsFilterHealingDone;
    public static int warlordsFilterHealingReceived;
    public static boolean warlordsFilterAbsorbtion;
    public static boolean warlordsFilterWounded;
    // add further options here

    // descriptions
    public static final String COIN_COUNTER = "Turn on/off the Coin Counter for ";
    public static final String TIMER = "Turn on/off the Time Display for ";
    public static final String MEGA_WALLS_KILL_COUNTER = "Enable/Disable the Kill and Assist Counter in";
    public static final String PAINTBALL_KILLSTREAK_TRACKER = "Show/Hide your active Paintball killstreaks and the ones on cooldown.";
    public static final String KILLSTREAK_TRACKER = "Enable/Disable tracking how many kills you can get within one live in ";
    public static final String BLITZ_DEATHMATCH_NOTIFIER = "Show/Hide a little flashing message when death match is near in Blitz Survival Games";
    public static final String WITHER_COIN_COUNTER = "Turn on/off the Counting of the Coins for Wither Damage in Mega Walls.";
    public static final String WARLORDS_FILTER_1 = "Filter out all chat message containing lower ";
    public static final String WARLORDS_FILTER_2 = " than this value.";

    public HudPixelConfig(File config) {
        // the "3" is the defined config version
        this.config = new Configuration(config, "3");
        this.config.load();

        // update the config if the versions mismatch
        if (this.config.getLoadedConfigVersion() == null || !this.config.getLoadedConfigVersion().equals(this.config.getDefinedConfigVersion())) {
            this.updateConfig(this.config.getLoadedConfigVersion());
        }
    }

    public void syncConfig() {
        useAPI = this.config.get(Configuration.CATEGORY_GENERAL, "useAPI", true, "Allow the usage of the Hypixel Public API. All features using the API won't work without it.").getBoolean(true);
        autoCompleteSecondLobbyCmd = this.config.get(Configuration.CATEGORY_GENERAL, "autoCompleteSecondLobbyCmd", false, "Automatically send the the second /lobby command for getting out of a game.").getBoolean(false);
        filterCoins = this.config.get(Configuration.CATEGORY_GENERAL, "filterCoins", 0,"Filter out all coin messages containing less coins than this value.").getInt();
        displayMode = this.config.get(DISPLAY_CATEGORY, "displayMode", "lefttop", "Choose where to render everything the mod displays.(\"lefttop\", \"righttop\", \"leftbottom\" and \"rightbottom\")").getString();
        displayXOffset = this.config.get(DISPLAY_CATEGORY, "xOffset", 0, "This value will be added to the X (horizontal) position before rendering.").getInt();
        displayYOffset = this.config.get(DISPLAY_CATEGORY, "yOffset", 0, "This value will be added to the Y (vertical) position before rendering.").getInt();
        displayShowResultTime = this.config.get(DISPLAY_CATEGORY, "showResultTime", 20, "How long (in seconds) the results will be shown after a game. Use -1 so it stays until the next game starts.").getInt(20);
        displayVersion = this.config.get(DISPLAY_CATEGORY, "displayVersion", true, "Show the mod version and name when there is nothing else to show.").getBoolean();
        displayNetworkBoosters = this.config.get(DISPLAY_CATEGORY, "displayNetworkBoosters", true, "Show active Network Boosters in the Chat Gui. This feature requires the Public API.").getBoolean(true);
        displayTipAllButton = this.config.get(DISPLAY_CATEGORY, "displayTipAllButton", false, "Show a button that runs /tip all.").getBoolean(false);
        quakeCoinDisplay = this.config.get(QUAKE_CATEGORY, "quakeCoinDisplay", true, COIN_COUNTER + "Quakecraft.").getBoolean(true);
        quakeTimeDisplay = this.config.get(QUAKE_CATEGORY, "quakeTimeDisplay", true, TIMER + "Quakecraft.").getBoolean(true);
        quakeKillstreakTracker = this.config.get(QUAKE_CATEGORY, "quakeKillstreakTracker", true, KILLSTREAK_TRACKER + "Quakecraft.").getBoolean(true);
        tntCoinDisplay = this.config.get(TNT_CATEGORY, "tntCoinDisplay", true, COIN_COUNTER + "the TNT Games").getBoolean(true);
        tntTimeDisplay = this.config.get(TNT_CATEGORY, "tntTimeDisplay", true, TIMER + "the TNT Games").getBoolean(true);
        tntKillstreakTracker = this.config.get(TNT_CATEGORY, "tntKillstreakTracker", true, KILLSTREAK_TRACKER + "TNT Wizards.").getBoolean(true);
        vampireCoinDisplay = this.config.get(VAMPIREZ_CATEGORY, "vampireZCoinDisplay", true, COIN_COUNTER + "VampireZ.").getBoolean(true);
        vampireTimeDisplay = this.config.get(VAMPIREZ_CATEGORY, "vampireZTimeDisplay", true, TIMER + "VampireZ.").getBoolean(true);
        vampireBalance = this.config.get(VAMPIREZ_CATEGORY, "vampireZBalance", true, "Turn on/off the advanced Coin Counter for VampireZ. This one tracks spent coins as well!").getBoolean(true);
        arcadeCoinDisplay = this.config.get(ARCADE_CATEGORY, "arcadeCoinDisplay", true, COIN_COUNTER + "the Arcade Games.").getBoolean(true);
        arcadeTimeDisplay = this.config.get(ARCADE_CATEGORY, "acrcadeTimeDisplay", true, TIMER + "the Arcade Games.").getBoolean(true);
        arcadeKillstreakTracker = this.config.get(ARCADE_CATEGORY, "arcadeKillstreakTracker", true, KILLSTREAK_TRACKER + "some Arcade Games.").getBoolean(true);
        wallsCoinDisplay = this.config.get(WALLS_CATEGORY, "wallsCoinDisplay", true, COIN_COUNTER + "the classic Walls.").getBoolean(true);
        wallsTimeDisplay = this.config.get(WALLS_CATEGORY, "wallsTimeDisplay", true, TIMER + "the classic Walls.").getBoolean(true);
        wallsKillCounter = this.config.get(WALLS_CATEGORY, "wallsKillCounter", true, MEGA_WALLS_KILL_COUNTER + "Walls").getBoolean(true);
        megaWallsCoinDisplay = this.config.get(MEGA_WALLS_CATEGORY, "megaWallsCoinDisplay", true, COIN_COUNTER + "Mega Walls.").getBoolean(true);
        megaWallsWitherCoinDisplay = this.config.get(MEGA_WALLS_CATEGORY, "megaWallsWitherCoinDisplay", true, WITHER_COIN_COUNTER).getBoolean(true);
        megaWallsKillCounter = this.config.get(MEGA_WALLS_CATEGORY, "megaWallsKillCounter", true, MEGA_WALLS_KILL_COUNTER + "Mega Walls.").getBoolean(true);
        blitzCoinDisplay = this.config.get(BLITZ_CATEGORY, "blitzCoinDisplay", true, COIN_COUNTER + "Blitz Survival Games.").getBoolean(true);
        blitzDeathmatchNotifier = this.config.get(BLITZ_CATEGORY, "blitzDeathmatchNotifier", true, BLITZ_DEATHMATCH_NOTIFIER).getBoolean(true);
        blitzStarTracker = this.config.get(BLITZ_CATEGORY, "blitzStarTracker", true).getBoolean(true);
        arenaCoinDisplay = this.config.get(ARENA_CATEGORY, "arenaCoinDisplay", true, COIN_COUNTER + "Arena Brawl.").getBoolean(true);
        paintballCoinDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballCoinDisplay", true, COIN_COUNTER + "Paintball Warfare.").getBoolean(true);
        paintballTimeDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballTimeDisplay", true, TIMER + "Paintball Warfare.").getBoolean(true);
        paintballKillstreakTrackerDisplay = this.config.get(PAINTBALL_CATEGORY, "paintballKillstreakTrackerDisplay", true, PAINTBALL_KILLSTREAK_TRACKER).getBoolean(true);
        copsAndCrimsCoinDisplay = this.config.get(COPS_AND_CRIMS_CATEGORY, "copsAndCrimsCoinDisplay", true, COIN_COUNTER + "Cops and Crims.").getBoolean(true);
        copsAndCrimsTimeDisplay = this.config.get(COPS_AND_CRIMS_CATEGORY, "copsAndCrimsTimeDisplay", true, TIMER + "Cops and Crims.").getBoolean(true);
        uhcCoinDisplay = this.config.get(UHC_CATEGORY, "uhcCoinDisplay", true, COIN_COUNTER + "UHC.").getBoolean(true);
        warlordsCoinDisplay = this.config.get(WARLORDS_CATEGORY, "warlordsCoinDisplay", true, COIN_COUNTER + "Warlords.").getBoolean(true);
        warlordsTimeDisplay = this.config.get(WARLORDS_CATEGORY, "warlordsTimeDisplay", true, TIMER + "Warlords.").getBoolean(true);
        warlordsDamageAndHealthCounter = this.config.get(WARLORDS_CATEGORY, "warlordsDamageAndHealthCounter", true, "Counts the damage and healing you do in a Warlords game.").getBoolean();
        warlordsFilterDamageTaken = this.config.get(WARLORDS_CATEGORY, "warlordsFilterDamageTaken", 0, WARLORDS_FILTER_1 + "Damage(taken)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterDamageDone = this.config.get(WARLORDS_CATEGORY, "warlordsFilterDamageDone", 0, WARLORDS_FILTER_1 + "Damage(done)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterHealingReceived = this.config.get(WARLORDS_CATEGORY, "warlordsFilterHealingReceived", 0, WARLORDS_FILTER_1 + "Healing(received)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterHealingDone = this.config.get(WARLORDS_CATEGORY, "warlordsFilterHealingDone", 0, WARLORDS_FILTER_1 + "Healing(done)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterAbsorbtion = this.config.get(WARLORDS_CATEGORY, "warlordsFilterAbsorbtion", false, "Filter out all chat messages containing information about absorbtion.").getBoolean();
        warlordsFilterWounded = this.config.get(WARLORDS_CATEGORY, "warlordsFilterWounded", false,"Filter out all chat messages containing information about being wounded.").getBoolean();
        // load further options here

        if (this.config.hasChanged()) {
            this.config.save();
        }
    }

    public Configuration getConfigFile() {
        return config;
    }

    // for the config gui introduced in 2.1.0, most properties need to be renamed
    private void updateConfig(String oldVersion) {
        // allows to update multiple versions simultaneously.
        boolean fallThrough = false;
        if (oldVersion == null) {
            fallThrough = true;

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
        }
        if (fallThrough || oldVersion.equals("2")) {
            fallThrough = true;

            this.config.get(DISPLAY_CATEGORY, "displayTipAllButton", false, "Show a button that runs /tip all.").set(false);;
        }
        this.config.save();
    }

    /**
     * Renames a property in a given category. But other than
     * Configuration.renameProperty(), it does turn it into a boolean property.
     * 
     * @param category
     *            the category in which the property resides
     * @param oldPropName
     *            the existing property name
     * @param newPropName
     *            the new property name
     * @return true if the category and property exist, false otherwise
     */
    public boolean renameBooleanProperty(String category, String oldPropName, String newPropName) {
        if (this.config.hasCategory(category)) {
            if (this.config.getCategory(category).containsKey(oldPropName) && !oldPropName.equalsIgnoreCase(newPropName)) {
                this.config.get(category, newPropName, this.config.getCategory(category).get(oldPropName).getBoolean());
                this.config.getCategory(category).remove(oldPropName);
                return true;
            }
        }
        return false;
    }
}
