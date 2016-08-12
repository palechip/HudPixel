/******************************************************************************
 * HudPixelExtended (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the
 * Hypixel Network.
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

package com.palechip.hudpixelmod.extended.configuration;

import com.palechip.hudpixelmod.config.FancyConfigElement;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static boolean isDebuging = false;

    public static boolean isPingShown = true;
    public static boolean isFpsShown = true;
    public static boolean isFancyChat = true;
    public static boolean isBoosterDisplay = true;
    public static boolean isFriendsDisplay = true;
    public static boolean isStats = true;
    public static boolean isHideOfflineFriends = true;
    public static int     storedMessages = 1000;
    public static int     displayMessages = 8;
    public static int     friendsShownAtOnce = 10;
    public static int     boostersShownAtOnce = 5;

    public static float hudRed = 0f;
    public static float hudGreen = 0f;
    public static float hudBlue = 0f;
    public static float hudAlpha = 1f;
    public static boolean hudBackground = true;

    public static boolean isHideCooldownDisplay = false;
    public static int xOffsetCooldownDisplay = 0;
    public static int yOffsetCooldownDisplay = 25;


    public static List<IConfigElement> getExtendedElements(Configuration configFile){

        List<IConfigElement> extendedSettings = new ArrayList<IConfigElement>();

        //extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "showFPS"         , true , "Show your current FPS in the HudPixel Gui.")));
        //extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "showPing"        , true , "Show your current Ping in the HudPixel Gui.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "OnlineFriends"   , true , "Activate or deactivate the online friends display in the pause menu.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "BoosterDisplay"   , true , "Activate or deactivate the booster display in the chat menu.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "ExternalChat"    , true , "Activate or deactivate the external chat. This will not stop storing messages.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "StatsDisplay"    , true , "Activate or deactivate the stats display above the player.")));
        extendedSettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "storedMessages"  , 1000 , "How many messages the external Chat Gui can store."), 100, 10000));
        extendedSettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "displayMessages" , 8    , "How long a detected message will be displayed on the bottom right."), 1, 30, GuiConfigEntries.NumberSliderEntry.class));
        extendedSettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "boostersShownAtOnce"  , 5 , "How many boosters are shown at once."), 1, 15));
        extendedSettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "friendsShownAtOnce"   , 10 , "How many friends are shown at once."), 1, 15));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "hideOfflineFriends"   , false , "Hide offline friends in the FriendsDisplay.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "hideCooldownDisplay"   , false , "Hide the cooldown display.")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "xOffsetCooldownDisplay"   , 0 , "x-Offset of the cooldown display")));
        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "yOffsetCooldownDisplay"   , 30 , "y-Offset of the cooldown display")));


        return extendedSettings;
    }



}
