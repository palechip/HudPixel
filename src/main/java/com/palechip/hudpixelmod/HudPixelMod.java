package com.palechip.hudpixelmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION)
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String VERSION = "1.0";
    private static HudPixelMod instance;

    public static final boolean IS_DEBUGGING = false;
    public static Logger LOGGER;

    private HypixelNetworkDetector hypixelDetector;
    private GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        instance = this;
        // Initialize the logger
        LOGGER = LogManager.getLogger("HudPixel");

        // register this class as an event handler
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        //initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);
    }

    @SubscribeEvent
    public void onGuiShow(GuiOpenEvent event) {
        try {
            // check if the player is on Hypixel Network
            // this can only change when a new gui is opened
            this.hypixelDetector.check();

            // pass the event to the GameDetector
            this.gameDetector.onGuiShow(event.gui);
        } catch(Exception e) {
            this.logWarn("An exception occured in onGuiShow(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        try {
            // pass the event to the GameDetector
            this.gameDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
            
            // check for start and stop
            this.gameStartStopDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
            
            // pass the chat messages to the current game
            if(this.gameDetector.getCurrentGame() != null && this.gameDetector.getCurrentGame().hasGameStarted()) {
                this.gameDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            // pass the event to the GameDetector
            this.gameDetector.onClientTick();
            
            // tick the current game
            if(this.gameDetector.getCurrentGame() != null && this.gameDetector.getCurrentGame().hasGameStarted()) {
                this.gameDetector.onClientTick();
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        try {
            //TODO: Add config for rendering
            int height = 2;
            int width = 2;
            if(HypixelNetworkDetector.isHypixelNetwork) {
                FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
                if(IS_DEBUGGING) {
                    fontRenderer.drawString("detectionStarted: " + gameDetector.isDetectionStarted(), width, height, 0xffffff);
                    height += 8;
                    fontRenderer.drawString("isInLobby: " + gameDetector.isInLobby(), width, height, 0xffffff);
                    height += 8;
                    if(gameDetector.getCurrentGame() != null) {
                        fontRenderer.drawString("currentGame: " + gameDetector.getCurrentGame(), width, height, 0xffffff);
                        height += 8;
                        fontRenderer.drawString("hasStarted: " + gameDetector.getCurrentGame().hasGameStarted(), width, height, 0xffffff);
                        height += 8;
                    }
                }
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
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
