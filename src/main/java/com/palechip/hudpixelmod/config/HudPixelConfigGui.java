/***********************************************************************************************************************
 HudPixelReloaded - License

 The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 intended for usage in this kind of application. By default, all rights are reserved.
 The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 The majority of code left from palechip's creations is the component implementation.The ported version to
 Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 (to be changed to the new license as detailed below in the next minor update).

 For the rest of the code and for the build the following license applies:

 # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

 Restrictions:

 The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 cases the authors reserve the right to revoke all rights for usage of the codebase.

 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 code, but only when it is used separately from HudPixel and any license header must indicate that.
 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 two of the authors.
 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 code is merged to the release branch you cannot revoke the given freedoms by this license.
 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 related files.
 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 reserve the right to take down any infringing project.
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.config;

@Deprecated
public class HudPixelConfigGui/* extends GuiConfig */{

   /* private static List<IConfigElement> getDisplayElements(Configuration configFile) {

        List<IConfigElement> displaySettings = new ArrayList<IConfigElement>();

        //displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayMode"            , "lefttop" , "Choose where to render everything the mod displays."), new String[] {"lefttop", "righttop","leftbottom", "rightbottom"}));
        //displaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayNetworkBoosters" , true      , "Show active Network Boosters in the Chat Gui. This feature requires the Public API.")));
        //displaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayQuickLoadButton" , false     , "Show a button that runs /booster queue in order to quickly load the network boosters.")));
        //displaySettings.add(new ConfigElement     (configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "displayVersion"         , true      , "Show the mod version and name when there is nothing else to show.")));
        //displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "showResultTime"         , 20        , "How long (in seconds) the results will be shown after a game. Use -1 so it stays until the next game starts."), -1, 600, NumberSliderEntry.class));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "xOffset", 5, "This value will be added to the X (horizontal) position before rendering."), 0, 4000));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "yOffset", 5, "This value will be added to the Y (vertical) position before rendering."), 0, 2000));

        displaySettings.add(new ConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "enableBackground", false, "Toggle the background of the hud.")));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "hudRed", 0, "Set the background color of the Hud."), 0, 255, GuiConfigEntries.NumberSliderEntry.class));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "hudGreen", 0, "Set the background color of the Hud."), 0, 255, GuiConfigEntries.NumberSliderEntry.class));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "hudBlue", 0, "Set the background color of the Hud."), 0, 255, GuiConfigEntries.NumberSliderEntry.class));
        displaySettings.add(new FancyConfigElement(configFile.get(HudPixelConfig.DISPLAY_CATEGORY, "hudAlpha", 0, "Set the background color of the Hud."), 0, 255, GuiConfigEntries.NumberSliderEntry.class));
        return displaySettings;
    }

    private static List<IConfigElement> getConfigElements() {

        List<IConfigElement> list = new ArrayList<IConfigElement>();

        List<IConfigElement> gameSettings = new ArrayList<IConfigElement>();
        Configuration configFile = HudPixelMod.instance().CONFIG.getConfigFile();

        // add the list to the main list
        list.add(new DummyCategoryElement(EnumChatFormatting.GOLD + "Extended Settings", "", Config.getExtendedElements(configFile)));
        list.add(new DummyCategoryElement(EnumChatFormatting.YELLOW + "General Settings", "", new ConfigElement(configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements()));
        list.add(new DummyCategoryElement(EnumChatFormatting.YELLOW + "Display Settings", "", getDisplayElements(configFile)));

        // Add a button for the current game if there is one
        Game currentGame = HudPixelMod.instance().gameDetector.getCurrentGame();
        if (!currentGame.equals(Game.NO_GAME))
            list.add(new DummyCategoryElement(WordUtils.capitalize(currentGame.getConfiguration().getConfigCategory()), "", new ConfigElement(configFile.getCategory(currentGame.getConfiguration().getConfigCategory())).getChildElements()));


        // Collect all categories
        Set<String> categories = new HashSet<String>();
        for (GameConfiguration gameConfig : GameManager.getGameManager().getConfigurations()) {
            // add the category, the set prevents that one category is added multiple times
            if (!gameConfig.getConfigCategory().isEmpty())
                categories.add(gameConfig.getConfigCategory());
        }

        // create entries in the gameSettings list
        for (String category : categories)
            // and make add an entry to the game settings list
            gameSettings.add(new DummyCategoryElement(EnumChatFormatting.GREEN + WordUtils.capitalize(category), "", new ConfigElement(configFile.getCategory(category)).getChildElements()));


        // add the gameSettings list
        list.add(new DummyCategoryElement(EnumChatFormatting.GREEN + "Game Settings", "", gameSettings));

        return list;
    }

    public HudPixelConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), HudPixelMod.MODID, false, false, "HudPixel Config");
    }*/
}
