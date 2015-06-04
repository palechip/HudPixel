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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.config.FancyConfigElement;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.games.GameConfiguration;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class HudPixelConfigGui extends GuiConfig {
    /**
     * Get a list of all Elements in the config
     */
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        List<IConfigElement> displaySettings = new ArrayList<IConfigElement>();
        List<IConfigElement> gameSettings = new ArrayList<IConfigElement>();
        Configuration configFile = HudPixelMod.instance().CONFIG.getConfigFile();
        
        // these categories have to be there always
        list.add(new DummyCategoryElement(EnumChatFormatting.YELLOW + "General Settings", "", new ConfigElement(configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements()));
        // Assemble the fancy controls for the displaySettings
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayMode", "lefttop", "Choose where to render everything the mod displays."), new String[] {"lefttop", "righttop","leftbottom", "rightbottom"}));
        displaySettings.add(new ConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayNetworkBoosters", true, "Show active Network Boosters in the Chat Gui. This feature requires the Public API.")));
        displaySettings.add(new ConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayQuickLoadButton", false, "Show a button that runs /booster queue in order to quickly load the network boosters.")));
        displaySettings.add(new ConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayVersion", true, "Show the mod version and name when there is nothing else to show.")));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "showResultTime", 20, "How long (in seconds) the results will be shown after a game. Use -1 so it stays until the next game starts."), -1, 600, NumberSliderEntry.class));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "xOffset", 0, "This value will be added to the X (horizontal) position before rendering."), 0, 4000));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "yOffset", 0, "This value will be added to the Y (vertical) position before rendering."), 0, 2000));
        // add the list to the main list
        list.add(new DummyCategoryElement(EnumChatFormatting.YELLOW + "Display Settings", "", displaySettings));
        
        // Add a button for the current game if there is one
        Game currentGame = HudPixelMod.instance().gameDetector.getCurrentGame();
        if (!currentGame.equals(Game.NO_GAME)) {
            list.add(new DummyCategoryElement(WordUtils.capitalize(currentGame.getConfiguration().getConfigCategory()), "", new ConfigElement(configFile.getCategory(currentGame.getConfiguration().getConfigCategory())).getChildElements()));
        }
        
        // Collect all categories
        Set<String> categories = new HashSet<String>();
        for(GameConfiguration gameConfig : GameManager.getGameManager().getConfigurations()) {
            // add the category, the set prevents that one category is added multiple times
            if(!gameConfig.getConfigCategory().isEmpty()) {
                categories.add(gameConfig.getConfigCategory());
            }
        }
        
        // create entries in the gameSettings list
        for(String category : categories) {
            // and make add an entry to the game settings list
            gameSettings.add(new DummyCategoryElement(WordUtils.capitalize(category), "", new ConfigElement(configFile.getCategory(category)).getChildElements()));
        }
        
        // add the gameSettings list
        list.add(new DummyCategoryElement(EnumChatFormatting.GREEN + "Game Settings", "", gameSettings));

        return list;
    }

    public HudPixelConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), HudPixelMod.MODID, false, false, "HudPixel Config");
    }
}
