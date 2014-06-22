package com.palechip.hudpixelmod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class HudPixelConfig {
    private Configuration config;
    
    public static final String QUAKE_CATEGORY = "Quake";
    // define further categories here
    
    public static boolean quakeCoinDisplay;
    public static boolean quakeTimeDisplay;
    // add further options here

    public HudPixelConfig(File config) {
        this.config = new Configuration(config);
    }
    
    public void loadConfig() {
        this.config.load();
        quakeCoinDisplay = this.config.get(QUAKE_CATEGORY, "coinDisplay", true).getBoolean(true);
        quakeTimeDisplay = this.config.get(QUAKE_CATEGORY, "timeDisplay", true).getBoolean(true);
        // load further options here
        this.config.save();
    }
}
