package com.palechip.hudpixelmod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class HudPixelConfigGui extends GuiConfig {
	/**
	 * Get a list of all Elements in the config
	 */
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		Configuration configFile = HudPixelMod.instance().CONFIG.getConfigFile();
		// unfortunately there is no array for this
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.DISPLAY_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.QUAKE_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.TNT_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.VAMPIREZ_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.ARCADE_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.WALLS_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.MEGA_WALLS_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.BLITZ_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.ARENA_CATEGORY)).getChildElements());
		list.addAll(new ConfigElement(configFile.getCategory(HudPixelConfig.PAINTBALL_CATEGORY)).getChildElements());
		return list;
	}

	public HudPixelConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), HudPixelMod.MODID, false, false, GuiConfig.getAbridgedConfigPath(HudPixelMod.instance().CONFIG.getConfigFile().toString()));
	}
}