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
package com.palechip.hudpixelmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;






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
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class HudPixelConfigGui extends GuiConfig {
    /**
     * Get a list of all Elements in the config
     */
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        Configuration configFile = HudPixelMod.instance().CONFIG.getConfigFile();
        // these categories have to be there always
        list.addAll(new ConfigElement(configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.DISPLAY_CATEGORY)).getChildElements());
        if (!HudPixelMod.instance().gameDetector.getCurrentGame().equals(Game.NO_GAME)) {
            // Add the current game
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelMod.instance().gameDetector.getCurrentGame().getConfiguration().getConfigCategory())).getChildElements());
            if (HypixelNetworkDetector.isHypixelNetwork) {
                new ChatMessageComposer("Opened the config for currently played game. Open in lobby to access all options.", EnumChatFormatting.DARK_GREEN).send();
            }
        } else {
            // Add all games
            // Collect all categories
            Set<String> categories = new HashSet<String>();
            for(GameConfiguration gameConfig : GameManager.getGameManager().getConfigurations()) {
                // add the category, the set prevents that one category is added multiple times
                categories.add(gameConfig.getConfigCategory());
            }
            for(String category : categories) {
                list.addAll(new ConfigElement(configFile.getCategory(category)).getChildElements());
            }
            if (HypixelNetworkDetector.isHypixelNetwork) {
                new ChatMessageComposer("Opened the config with all options. Tip: If you are in a game you'll only see relevant options.", EnumChatFormatting.DARK_GREEN).send();
            }
        }
        return list;
    }

    public HudPixelConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), HudPixelMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(HudPixelMod.instance().CONFIG.getConfigFile().toString()));
    }
}