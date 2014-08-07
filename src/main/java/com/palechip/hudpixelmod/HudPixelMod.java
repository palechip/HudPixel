package com.palechip.hudpixelmod;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

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

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION)
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String VERSION = "1.0.2";
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

    // Rendering vars
    private boolean renderOnTheRight;
    private int startWidth;
    private int startHeight;
    
    // vars for displaying the results after a game
    private ArrayList<String> results;
    private long resultRenderTime;
    private long resultStartTime;
    
    // key related vars
    public static final String KEY_CATEGORY = "HudPixel Mod";
    private KeyBinding hideHUDKey;
    private boolean isHUDShown = true;

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

        // initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);

        // initialize rendering vars
        this.startHeight = HudPixelConfig.displayYOffset + 1;
        this.renderOnTheRight = HudPixelConfig.displayMode.toLowerCase().equals("right");
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        if(this.renderOnTheRight) {
            this.startWidth = (res.getScaledWidth() + HudPixelConfig.displayXOffset) - 1;
        } else {
            this.startWidth = HudPixelConfig.displayXOffset + 1;
        }
        
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
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            // update the resolution for rendering on the right
            if(this.renderOnTheRight) {
                Minecraft mc = FMLClientHandler.instance().getClient();
                ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                this.startWidth = (res.getScaledWidth() + + HudPixelConfig.displayXOffset) - 1;
            }
            
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

            // check if the update message can be displayed
            if(this.isUpdateMessageQueued) {
                this.updateFound();
            }
            if(this.results != null) {
                if((System.currentTimeMillis() - this.resultStartTime) >= this.resultRenderTime) {
                    this.results = null;
                }
            }
            

        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        try {
            Minecraft mc = FMLClientHandler.instance().getClient();
            if(HypixelNetworkDetector.isHypixelNetwork && !mc.gameSettings.showDebugInfo && (mc.inGameHasFocus || mc.currentScreen instanceof GuiChat) && (this.gameDetector.getCurrentGame() != null || this.results != null) && this.isHUDShown) {
                FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
                int width;
                int height = this.startHeight;
                ArrayList<String> renderStrings;
                if(this.gameDetector.getCurrentGame() != null) {
                    renderStrings = this.gameDetector.getCurrentGame().getRenderStrings();
                } else {
                    renderStrings = this.results;
                }
                
                // get the right width
                if(this.renderOnTheRight) {
                    int maxWidth = 0;
                    for(String s : renderStrings) {
                        if(s != null) {
                            int stringWidth = mc.fontRenderer.getStringWidth(s);
                            if(stringWidth > maxWidth) {
                                maxWidth = stringWidth;
                            }
                        }
                    }
                    width = this.startWidth - maxWidth;
                } else {
                    width = this.startWidth;
                }
                
                // render the game
                for(int i = 0; i < renderStrings.size(); i++) {
                    // skip the string if it's empty
                    if(renderStrings.get(i) != null && !renderStrings.get(i).isEmpty()) {
                        fontRenderer.drawString(renderStrings.get(i), width, height, 0xffffff);
                        height += RENDERING_HEIGHT_OFFSET;
                    }
                }
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // check all listened keys
        if(this.hideHUDKey.isPressed()) {
            this.isHUDShown = !this.isHUDShown;
        }
    }
    
    // called with the last set of rendering strings
    public void displayResults(ArrayList<String> results) {
        this.results = results;
        this.resultStartTime = System.currentTimeMillis();
        this.resultRenderTime = HudPixelConfig.displayShowResultTime >= 0 ? HudPixelConfig.displayShowResultTime * 1000 : Integer.MAX_VALUE; // transform to milliseconds
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
