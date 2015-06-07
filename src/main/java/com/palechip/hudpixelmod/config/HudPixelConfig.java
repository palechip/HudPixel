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
package com.palechip.hudpixelmod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.palechip.hudpixelmod.components.IComponent;
import com.palechip.hudpixelmod.games.ComponentsManager;
import com.palechip.hudpixelmod.games.GameConfiguration;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.util.GameType;

import net.minecraftforge.common.config.Configuration;

public class HudPixelConfig {
    private Configuration config;

    // categories
    // when adding a new category, don't forget to add it to HudPixelConfigGui
    public static final String DISPLAY_CATEGORY = "display";
    // all game related categories are created dynamically using GameConfiguration.getConfigCategory()
    
    // static properties
    public static boolean useAPI;
    public static boolean enableAfterStats;
    public static boolean autoCompleteSecondLobbyCmd;
    public static String displayMode;
    public static int displayXOffset;
    public static int displayYOffset;
    public static int displayShowResultTime;
    public static boolean displayVersion;
    public static boolean displayNetworkBoosters;
    public static boolean displayQuickLoadButton;
    
    public static int warlordsFilterDamageDone;
    public static int warlordsFilterDamageTaken;
    public static int warlordsFilterHealingDone;
    public static int warlordsFilterHealingReceived;
    public static boolean warlordsFilterAbsorbtion;
    public static boolean warlordsFilterWounded;
    
    // non-static properties
    private static HashMap<String, Boolean> properties = new HashMap<String, Boolean>();
    // add further options here

    // descriptions 
    public static final String WARLORDS_FILTER_1 = "Filter out all chat message containing lower ";
    public static final String WARLORDS_FILTER_2 = " than this value.";

    public HudPixelConfig(File config) {
        // the "3" is the defined config version
        this.config = new Configuration(config, "4");
        this.config.load();

        // update the config if the versions mismatch
        if (this.config.getLoadedConfigVersion() == null || !this.config.getLoadedConfigVersion().equals(this.config.getDefinedConfigVersion())) {
            this.updateConfig(this.config.getLoadedConfigVersion());
        }
    }

    /**
     * Reads the config. Also creates new properties. Doesn't change the file but saves it if it has changed.
     */
    public void syncConfig() {
        useAPI = this.config.get(Configuration.CATEGORY_GENERAL, "useAPI", true, "Allow the usage of the Hypixel Public API. All features using the API won't work without it.").getBoolean(true);
        enableAfterStats = this.config.get(Configuration.CATEGORY_GENERAL, "enableAfterStats", true, "Display statistics of the player who killed or beat you. (Not all games supported)").getBoolean();
        autoCompleteSecondLobbyCmd = this.config.get(Configuration.CATEGORY_GENERAL, "autoCompleteSecondLobbyCmd", false, "Automatically send the the second /lobby command for getting out of a game.").getBoolean(false);
        displayMode = this.config.get(DISPLAY_CATEGORY, "displayMode", "lefttop", "Choose where to render everything the mod displays.(\"lefttop\", \"righttop\", \"leftbottom\" and \"rightbottom\")").getString();
        displayXOffset = this.config.get(DISPLAY_CATEGORY, "xOffset", 0, "This value will be added to the X (horizontal) position before rendering.").getInt();
        displayYOffset = this.config.get(DISPLAY_CATEGORY, "yOffset", 0, "This value will be added to the Y (vertical) position before rendering.").getInt();
        displayShowResultTime = this.config.get(DISPLAY_CATEGORY, "showResultTime", 20, "How long (in seconds) the results will be shown after a game. Use -1 so it stays until the next game starts.").getInt(20);
        displayVersion = this.config.get(DISPLAY_CATEGORY, "displayVersion", true, "Show the mod version and name when there is nothing else to show.").getBoolean();
        displayNetworkBoosters = this.config.get(DISPLAY_CATEGORY, "displayNetworkBoosters", true, "Show active Network Boosters in the Chat Gui. This feature requires the Public API.").getBoolean(true);
        displayQuickLoadButton = this.config.get(DISPLAY_CATEGORY, "displayQuickLoadButton", true, "Show a button that runs /booster queue in order to quickly load the network boosters.").getBoolean(true);
        
        // these are ugly and will be removed when the filter becomes a component
        String warlordsCategory = GameManager.getGameManager().getGameConfiguration(GameType.WARLORDS).getConfigCategory();
        warlordsFilterDamageTaken = this.config.get(warlordsCategory, "warlordsFilterDamageTaken", 0, WARLORDS_FILTER_1 + "Damage(taken)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterDamageDone = this.config.get(warlordsCategory, "warlordsFilterDamageDone", 0, WARLORDS_FILTER_1 + "Damage(done)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterHealingReceived = this.config.get(warlordsCategory, "warlordsFilterHealingReceived", 0, WARLORDS_FILTER_1 + "Healing(received)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterHealingDone = this.config.get(warlordsCategory, "warlordsFilterHealingDone", 0, WARLORDS_FILTER_1 + "Healing(done)" + WARLORDS_FILTER_2).getInt();
        warlordsFilterAbsorbtion = this.config.get(warlordsCategory, "warlordsFilterAbsorbtion", false, "Filter out all chat messages containing information about absorbtion.").getBoolean();
        warlordsFilterWounded = this.config.get(warlordsCategory, "warlordsFilterWounded", false,"Filter out all chat messages containing information about being wounded.").getBoolean();

        // parse the non-static variables
        // clear the list first
        this.properties.clear();
        // create local variables to make the code clearer
        GameManager gameManager = GameManager.getGameManager();
        ComponentsManager componentsManager = gameManager.getComponentsManager();
        // this is used to have consistent naming for arcade games, tnt games, ...
        HashMap<String, String> categoryGameNames = new HashMap<String, String>();
        // go through all games
        for(GameConfiguration gameConfig : gameManager.getConfigurations()) {
            // save the game name for the category
            if(!categoryGameNames.containsKey(gameConfig.getConfigCategory())) {
                categoryGameNames.put(gameConfig.getConfigCategory(), gameConfig.getOfficialName());
            }
            // go through all components of the game
            for(IComponent component : componentsManager.getComponentInstances(gameConfig, true)) {
                String settingName = gameConfig.getConfigPrefix() + component.getConfigName();
                // check if we haven't already parsed this setting
                if(!properties.containsKey(settingName)) {
                    // generate the comment
                    String comment = component.getConfigComment().replace("%game", categoryGameNames.get(gameConfig.getConfigCategory()));
                    // read (and create if it doesn't exist) the property and add it to the properties map
                    this.properties.put(settingName, this.config.get(gameConfig.getConfigCategory(), settingName, component.getConfigDefaultValue(), comment).getBoolean());
                }
            }
        }

        if (this.config.hasChanged()) {
            this.config.save();
        }
    }

    /**
     * Get the value of a boolean dynamic setting
     * @param settingName GameConfiguration.getConfigPrefix() + IComponent.getConfigName()
     * @return the state of the setting. If the setting wasn't found, it'll return false
     */
    public static boolean getConfigValue(String settingName) {
        if(properties.containsKey(settingName)) {
            return properties.get(settingName);
        } else {
            return false;
        }
    }

    

    public Configuration getConfigFile() {
        return config;
    }

    /**
     * This helps not to lose data and can be used to update properties values and names.
     * @param oldVersion the old config version
     */
    private void updateConfig(String oldVersion) {
        // allows to update multiple versions simultaneously.
        boolean fallThrough = false;
        if (oldVersion == null) {
            fallThrough = true;
            // nobody should be updating from 2.1.0 anymore...
        }
        if (fallThrough || oldVersion.equals("2")) {
            fallThrough = true;

            // this isn't necessary anymore since it is undone with version 4
            //this.config.get(DISPLAY_CATEGORY, "displayTipAllButton", false, "Show a button that runs /tip all.").set(false);
        }
        if (fallThrough || oldVersion.equals("3")) {
            fallThrough = true;
            // correct a little typo
            this.renameBooleanProperty("arcade games", "acrcadeTimeDisplay", "arcadeTimeDisplay");
            this.renameBooleanProperty(DISPLAY_CATEGORY, "displayTipAllButton", "displayQuickLoadButton");
            this.config.get(DISPLAY_CATEGORY, "displayQuickLoadButton", true, "Show a button that runs /booster queue in order to quickly load the network boosters.").set(true);
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
