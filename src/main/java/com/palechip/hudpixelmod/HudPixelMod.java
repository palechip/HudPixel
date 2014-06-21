package com.palechip.hudpixelmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION)
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String VERSION = "1.0";
    private static HudPixelMod instance;
    
    public static final boolean IS_DEBUGGING = false;
    public static Logger LOGGER;
    
    private HypixelNetworkDetector hypixelDetector;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	instance = this;
    	// Initialize the logger
    	LOGGER = LogManager.getLogger("HudPixel");
    	
    	// register this class as an event handler
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	//initialize stuff
    	this.hypixelDetector = new HypixelNetworkDetector();
    }
    
    @SubscribeEvent
    public void onGuiShow(GuiOpenEvent event) {
    	try {
    		// check if the player is on Hypixel Network
    		// this can only change when a new gui is opened
    		this.hypixelDetector.check();
    	} catch(Exception e) {
    		this.logWarn("An exception occured in onGuiShow(). Stacktrace below.");
    		e.printStackTrace();
    	}
    }
    
    public static HudPixelMod instance() {
    	return instance;
    }
    
    public void logDebug(String s) {
    	if(IS_DEBUGGING) {
    		LOGGER.info("[DEBUG] "  + s);
    	}
    }
    
    public void logInfo(String s) {
    	LOGGER.info(s);
    }
    
    public void logWarn(String s) {
    	LOGGER.warn(s);
    }
    
    public void logError(String s) {
    	LOGGER.error(s); 	
    }
}
