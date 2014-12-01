package com.palechip.hudpixelmod;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.server.gui.PlayerListComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION, name = HudPixelMod.NAME, guiFactory = "com.palechip.hudpixelmod.HudPixelGuiFactory")
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String NAME = "HudPixel Reloaded";
    public static final String VERSION = "2.1.0";
    public static final boolean IS_DEBUGGING = false;
    public static final String HUDPIXEL_CHAT_PREFIX = "[" + EnumChatFormatting.RED + "HudPixel" + EnumChatFormatting.RESET + "] ";

    private static HudPixelMod instance;

    public Logger LOGGER;
    public HudPixelConfig CONFIG;
    private HudPixelUpdateNotifier  updater;
    private boolean isUpdateMessageQueued;
    public HudPixelRenderer renderer;
    private Queue apiQueue;

    private HypixelNetworkDetector hypixelDetector;
    public GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    // key related vars
    public static final String KEY_CATEGORY = "HudPixel Mod";
    private KeyBinding hideHUDKey;

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
            this.CONFIG.syncConfig();
            // Load the API key
            this.apiQueue = new Queue();
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

        // initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);
        this.renderer = new HudPixelRenderer(this.updater);

        // initializse key bindings
        this.hideHUDKey = new KeyBinding("Hide HUD", Keyboard.KEY_F9, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(this.hideHUDKey);
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

            // pass the message to the api connection
            this.apiQueue.onChatMessage(event.message.getUnformattedText());
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            // update the resolution and the result display, this renders nothing
            this.renderer.onClientTick();
            
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

            this.apiQueue.onClientTick();

            // check if the update message can be displayed
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
            // render the game
            this.renderer.onRenderTick();
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // check all listened keys
        if(this.hideHUDKey.isPressed()) {
            this.renderer.isHUDShown = !this.renderer.isHUDShown;
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(MODID)){
            this.CONFIG.syncConfig();
            // reload stuff that uses the config values for immediate effect
            this.renderer.loadRenderingProperties(updater);
            Game.loadGames();
        }
    }

    public void updateFound() {
        try {
            if(hypixelDetector.isHypixelNetwork && FMLClientHandler.instance().getClientPlayerEntity() != null) {
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HUDPIXEL_CHAT_PREFIX + EnumChatFormatting.DARK_PURPLE + "Update available: " + EnumChatFormatting.GREEN + this.updater.newVersion));
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HUDPIXEL_CHAT_PREFIX + EnumChatFormatting.DARK_PURPLE + "Download here: " + EnumChatFormatting.YELLOW + this.updater.downloadLink));
                this.isUpdateMessageQueued = false;
                // also update rendering vars to make sure it notices the update
                this.renderer.loadRenderingProperties(this.updater);
                
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
