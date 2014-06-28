package com.palechip.hudpixelmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION)
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String VERSION = "1.0.0";
    public static final boolean IS_DEBUGGING = false;
    public static final int RENDERING_HEIGHT_OFFSET = 10;

    private static HudPixelMod instance;

    public Logger LOGGER;
    public HudPixelConfig CONFIG;
    private HudPixelUpdateNotifier  updater;
    private boolean isUpdateMessageQueued;

    private HypixelNetworkDetector hypixelDetector;
    public GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            instance = this;
            // check for updates
            this.updater = new HudPixelUpdateNotifier();
            // Initialize the logger
            this.LOGGER = LogManager.getLogger("HudPixel");
            // load the configuration file
            this.CONFIG = new HudPixelConfig(event.getSuggestedConfigurationFile());
            this.CONFIG.loadConfig();
        } catch(Exception e) {
            this.logWarn("An exception occured in preInit(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
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

            // pass the chat messages to the current game
            if(this.gameDetector.getCurrentGame() != null && this.gameDetector.getCurrentGame().hasGameStarted()) {
                this.gameDetector.getCurrentGame().onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
            }

            // check for start and stop
            this.gameStartStopDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
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

            if(this.gameDetector.getCurrentGame() != null) {
                // tick the current game
                if(this.gameDetector.getCurrentGame().hasGameStarted()) {
                    this.gameDetector.getCurrentGame().onTickUpdate();
                }
                // update render strings
                this.gameDetector.getCurrentGame().updateRenderStrings();
            }

            if(this.isUpdateMessageQueued) {
                this.updateFound();
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
                    height += RENDERING_HEIGHT_OFFSET;
                    fontRenderer.drawString("isInLobby: " + gameDetector.isInLobby(), width, height, 0xffffff);
                    height += RENDERING_HEIGHT_OFFSET;
                    if(gameDetector.getCurrentGame() != null) {
                        fontRenderer.drawString("currentGame: " + gameDetector.getCurrentGame(), width, height, 0xffffff);
                        height += RENDERING_HEIGHT_OFFSET;
                        fontRenderer.drawString("hasStarted: " + gameDetector.getCurrentGame().hasGameStarted(), width, height, 0xffffff);
                        height += RENDERING_HEIGHT_OFFSET;
                    }
                }
                // render the game
                if(this.gameDetector.getCurrentGame() != null) {
                    Game currentGame = this.gameDetector.getCurrentGame();
                    for(int i = 0; i < currentGame.getRenderStrings().size(); i++) {
                        fontRenderer.drawString(currentGame.getRenderStrings().get(i), width, height, 0xffffff);
                        height += RENDERING_HEIGHT_OFFSET;
                    }
                }
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    public void updateFound() {
        try {
            if(hypixelDetector.isHypixelNetwork && FMLClientHandler.instance().getClientPlayerEntity() != null) {
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "HudPixel" + EnumChatFormatting.RESET + "] " + EnumChatFormatting.DARK_PURPLE + "Update available: " + EnumChatFormatting.GREEN + this.updater.newVersion));
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText("[" + EnumChatFormatting.RED + "HudPixel" + EnumChatFormatting.RESET + "] " + EnumChatFormatting.DARK_PURPLE + "Download here: " + EnumChatFormatting.YELLOW + this.updater.downloadLink));
                this.isUpdateMessageQueued = false;
            } else {
                // make this being called from onTick()
                this.isUpdateMessageQueued = true;
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in updateFound(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    public static HudPixelMod instance() {
        return instance;
    }

    public void logDebug(String s) {
        if(IS_DEBUGGING) {
            this.LOGGER.info("[DEBUG] "  + s);
        }
    }

    public void logInfo(String s) {
        this.LOGGER.info(s);
    }

    public void logWarn(String s) {
        this.LOGGER.warn(s);
    }

    public void logError(String s) {
        this.LOGGER.error(s); 	
    }
}
