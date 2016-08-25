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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class Config {

//######################################################################################################################

    public static boolean isDebuging = false;
    public static boolean isPingShown = true;
    public static boolean isFpsShown = true;

//STATSDISPLAY------------------------------------------------------------------------
    public static boolean isStats = true;

//FRIENDSDISPLAY----------------------------------------------------------------------
    public static int     friendsShownAtOnce = 10;
    public static boolean isHideOfflineFriends = true;
    public static boolean isFriendsDisplay = true;
    public static int xOffsetFriendsDisplay = 2;
    public static int yOffsetFriendsDisplay = 2;
    public static boolean shownFriendsDisplayRight = false;

//BOOSTERDISPLAY----------------------------------------------------------------------
    public static boolean isBoosterDisplay = true;
    public static int     boostersShownAtOnce = 5;
    public static int xOffsetBoosterDisplay = 2;
    public static int yOffsetBoosterDisplay = 2;
    public static boolean shownBooosterDisplayRight = true;

//COOLDOWNDISPLAY---------------------------------------------------------------------
    public static boolean isHideCooldownDisplay = false;
    public static int xOffsetCooldownDisplay = 0;
    public static int yOffsetCooldownDisplay = 25;

//CHATDISPLAY-------------------------------------------------------------------------
    public static int     storedMessages = 1000;
    public static int     displayMessages = 8;
    public static boolean isFancyChat = true;

//HUD---------------------------------------------------------------------------------
    public static float hudRed = 0f;
    public static float hudGreen = 0f;
    public static float hudBlue = 0f;
    public static float hudAlpha = 1f;
    public static boolean hudBackground = true;

//######################################################################################################################

    public static List<IConfigElement> getExtendedElements(Configuration configFile)
    {
        List<IConfigElement> extendedSettings = new ArrayList<IConfigElement>();

        //extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "showFPS"         , true , "Show your current FPS in the HudPixel Gui.")));
        //extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "showPing"        , true , "Show your current Ping in the HudPixel Gui.")));

        extendedSettings.add(new DummyConfigElement.DummyCategoryElement(EnumChatFormatting.GOLD  + "FriendsDisplay"    , "", getFriendsDisplayElements(configFile)));
        extendedSettings.add(new DummyConfigElement.DummyCategoryElement(EnumChatFormatting.GOLD  + "BoosterDisplay"    , "", getBoosterDisplayElements(configFile)));
        extendedSettings.add(new DummyConfigElement.DummyCategoryElement(EnumChatFormatting.GOLD  + "CooldownDisplay"   , "", getCooldownDisplayElements(configFile)));
        extendedSettings.add(new DummyConfigElement.DummyCategoryElement(EnumChatFormatting.GOLD  + "ChatDisplay"       , "", getChatDisplayElements(configFile)));

        extendedSettings.add(new ConfigElement     (configFile.get(HudPixelConfig.EXTENDED_CATEGORY, "StatsDisplay"    , true , "Activate or deactivate the stats display above the player.")));


        return extendedSettings;
    }

    private static List<IConfigElement> getFriendsDisplayElements(Configuration configFile)
    {
        List<IConfigElement> friendsdisplaySettings = new ArrayList<IConfigElement>();

        friendsdisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "OnlineFriends"   , true , "Activate or deactivate the online friends display in the pause menu.")));
        friendsdisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "hideOfflineFriends"   , false , "Hide offline friends in the FriendsDisplay.")));
        friendsdisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "friendsShownAtOnce"   , 10 , "How many friends are shown at once."), 1, 15));
        friendsdisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "xOffset"                , 2 , "This value will be added to the X (horizontal) position before rendering."), 0, 4000));
        friendsdisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "yOffset"                , 2 , "This value will be added to the Y (vertical) position before rendering."), 0, 2000));
        friendsdisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.FRIENDSDISPLAY_CATEGORY, "showRightSide"   , false , "Puts the display right on the screen.")));

        return friendsdisplaySettings;
    }

    private static List<IConfigElement> getBoosterDisplayElements(Configuration configFile)
    {
        List<IConfigElement> boosterDisplaySettings = new ArrayList<IConfigElement>();

        boosterDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.BOOSTERDISPLAY_CATEGORY, "BoosterDisplay"   , true , "Activate or deactivate the booster display in the chat menu.")));
        boosterDisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.BOOSTERDISPLAY_CATEGORY, "boostersShownAtOnce"  , 5 , "How many boosters are shown at once."), 1, 15));
        boosterDisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.BOOSTERDISPLAY_CATEGORY, "xOffset"                , 2 , "This value will be added to the X (horizontal) position before rendering."), 0, 4000));
        boosterDisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.BOOSTERDISPLAY_CATEGORY, "yOffset"                , 2 , "This value will be added to the Y (vertical) position before rendering."), 0, 2000));
        boosterDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.BOOSTERDISPLAY_CATEGORY, "showRightSide"   , true , "Puts the display right on the screen.")));

        return boosterDisplaySettings;
    }

    private static List<IConfigElement> getCooldownDisplayElements(Configuration configFile)
    {
        List<IConfigElement> cooldownDisplaySettings = new ArrayList<IConfigElement>();

        cooldownDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.COOLDOWN_CATEGORY, "hideCooldownDisplay"   , false , "Hide the cooldown display.")));
        cooldownDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.COOLDOWN_CATEGORY, "xOffsetCooldownDisplay"   , 0 , "x-Offset of the cooldown display")));
        cooldownDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.COOLDOWN_CATEGORY, "yOffsetCooldownDisplay"   , 30 , "y-Offset of the cooldown display")));


        return cooldownDisplaySettings;
    }

    private static List<IConfigElement> getChatDisplayElements(Configuration configFile)
    {
        List<IConfigElement> chatDisplaySettings = new ArrayList<IConfigElement>();

        chatDisplaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.CHAT_CATEGORY, "ExternalChat"    , true , "Activate or deactivate the external chat. This will not stop storing messages.")));
        chatDisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.CHAT_CATEGORY, "storedMessages"  , 1000 , "How many messages the external Chat Gui can store."), 100, 10000));
        chatDisplaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.CHAT_CATEGORY, "displayMessages" , 8    , "How long a detected message will be displayed on the bottom right."), 1, 30, GuiConfigEntries.NumberSliderEntry.class));


        return chatDisplaySettings;
    }
}
