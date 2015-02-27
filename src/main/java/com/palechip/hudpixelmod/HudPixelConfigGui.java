package com.palechip.hudpixelmod;

import java.util.ArrayList;
import java.util.List;

import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
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
        if (HudPixelMod.instance().gameDetector.getCurrentGame() != null) {
            // Add the current game
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelMod.instance().gameDetector.getCurrentGame().getConfigCategory())).getChildElements());
            if (HypixelNetworkDetector.isHypixelNetwork) {// only send chat msg
                                                          // if on server
                new ChatMessageComposer("Opened the config for currently played game. Open in lobby to access all options.", EnumChatFormatting.DARK_GREEN).send();
            }
        } else {
            // Add all games
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.QUAKE_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.TNT_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.VAMPIREZ_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.ARCADE_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.WALLS_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.MEGA_WALLS_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.BLITZ_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.ARENA_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.PAINTBALL_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.COPS_AND_CRIMS_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.UHC_CATEGORY)).getChildElements());
            list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.WARLORDS_CATEGORY)).getChildElements());
            new ChatMessageComposer("Opened the config with all options. Tipp: If you are in a game you'll only see relevant options.", EnumChatFormatting.DARK_GREEN).makeHover(new ChatMessageComposer("Test", EnumChatFormatting.GOLD)).send();
        }
        return list;
    }

    public HudPixelConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), HudPixelMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(HudPixelMod.instance().CONFIG.getConfigFile().toString()));
    }
}