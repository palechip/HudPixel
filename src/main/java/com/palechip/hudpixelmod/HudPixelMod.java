/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;
import com.palechip.hudpixelmod.command.GameCommand;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.config.HudPixelConfigGui;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.extended.HudPixelExtended;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.games.LoadGameConfigThread;
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper;
import com.palechip.hudpixelmod.modulargui.modules.PlayGameModularGuiProvider;
import com.palechip.hudpixelmod.util.ScoreboardReader;
import eladkay.modulargui.lib.Renderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import static com.palechip.hudpixelmod.HudPixelMod.SHORT_VERSION;

@Mod(
        modid = HudPixelMod.MODID,
        version = SHORT_VERSION,
        name = HudPixelMod.NAME,
        guiFactory = "com.palechip.hudpixelmod.config.HudPixelGuiFactory",
        clientSideOnly = true,
        acceptedMinecraftVersions = "1.8.9"
)

public class HudPixelMod {
    public static final String MODID = "hudpixel";
    static final String NAME = "HudPixel Reloaded";
    public static final String SHORT_VERSION = "3.0"; // only to be used for the annotation which requires such a constant.
    public static final String DEFAULT_VERSION = "3.1.3";
    public static final boolean IS_DEBUGGING = false;

    private static HudPixelMod instance;

    private Logger LOGGER;
    public HudPixelConfig CONFIG;
    public HudPixelRenderer renderer;
    private Queue apiQueue;
    
    private boolean deactivate = false;

    private HypixelNetworkDetector hypixelDetector;
    public GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    // key related vars
    private static final String KEY_CATEGORY = "HudPixel Mod";
    private KeyBinding hideHUDKey;
    private KeyBinding openConfigGui;
    private KeyBinding debugKey; // A key used to bind some debugging functionality.
    private KeyBinding pressToPlay;

    private WarlordsDamageChatFilter warlordsChatFilter;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            instance = this;

            ClientCommandHandler.instance.registerCommand(new GameCommand());
            // Initialize the logger
            this.LOGGER = LogManager.getLogger("HudPixel");

            new LoadGameConfigThread(event.getModConfigurationDirectory());

            // load the configuration file (this doesn't read it, it will only be read after the UpToDateThread finished processing games.json
            this.CONFIG = new HudPixelConfig(event.getSuggestedConfigurationFile());
            this.apiQueue = new Queue();
        } catch(Exception e) {
            this.logWarn("An exception occured in preInit(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // register this class as an event handler (but fn not because modular gui :3)
       // if(!IS_DEBUGGING) {
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
       // }
        MinecraftForge.EVENT_BUS.register(new Renderer());
        MinecraftForge.EVENT_BUS.register(new ModularGuiHelper());
        ModularGuiHelper.init();

        // setup HudPixelExtended
        HudPixelExtended.getInstance().setup();

        // initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);
        this.renderer = new HudPixelRenderer();
        this.warlordsChatFilter = new WarlordsDamageChatFilter();

        // Initialize key bindings
        this.hideHUDKey = new KeyBinding("Hide HUD", Keyboard.KEY_F9, KEY_CATEGORY);
        this.openConfigGui = new KeyBinding("Open Config", Keyboard.KEY_Q, KEY_CATEGORY);
        this.pressToPlay = new KeyBinding("Press this key to play the game set in the Modular GUI", Keyboard.KEY_P, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(this.hideHUDKey);
        ClientRegistry.registerKeyBinding(this.openConfigGui);
        ClientRegistry.registerKeyBinding(this.pressToPlay);
        if(this.IS_DEBUGGING) {
            this.debugKey = new KeyBinding("DEBUG KEY", Keyboard.KEY_J, KEY_CATEGORY);
            ClientRegistry.registerKeyBinding(this.debugKey);
        }
        
        new WarlordsCTFCrashPrevention();
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

    @SubscribeEvent(receiveCanceled=true)
    public void onChatMessage(ClientChatReceivedEvent event) {
        try {
            //Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {

                // this one reads the normal chat messages
                if(event.type == 0) {

                    // pass the event to the GameDetector
                    this.gameDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());

                    // pass the chat messages to the current game
                    if(!this.gameDetector.getCurrentGame().equals(Game.NO_GAME) && this.gameDetector.getCurrentGame().hasGameStarted()) {
                        this.gameDetector.getCurrentGame().onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
                    }

                    // check for start and stop
                    this.gameStartStopDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());

                    // pass the message to the api connection
                    this.apiQueue.onChatMessage(event.message.getUnformattedText());


                    //send event to Warlords damage chat disabler
                    this.warlordsChatFilter.onChat(event);

                    // this one are the messages on the status bar
                } else {
                    // not used right now
                }
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {

            // Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {
                // make sure the Scoreboard reader updates when necessary
                ScoreboardReader.resetCache();

                // update the resolution and the result display, this renders nothing
                this.renderer.onClientTick();

                // pass the event to the GameDetector
                this.gameDetector.onClientTick();

                if(!this.gameDetector.getCurrentGame().equals(Game.NO_GAME)) {
                    // tick the current game
                    if(this.gameDetector.getCurrentGame().hasGameStarted()) {
                        this.gameDetector.getCurrentGame().onTickUpdate();
                    }

                    // update render strings
                    this.gameDetector.getCurrentGame().updateRenderStrings();
                }

                this.apiQueue.onClientTick();

            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }


    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        try {
            // Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {
                // check all listened keys
                if(this.hideHUDKey.isPressed()) {
                    this.renderer.isHUDShown = !this.renderer.isHUDShown;
                }
                if(this.openConfigGui.isPressed()) {
                    // open the config screen
                    FMLClientHandler.instance().getClient().displayGuiScreen(new HudPixelConfigGui(null));
                }
                if(this.pressToPlay.isPressed()) {
                    // open the config screen
                    FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/play " + PlayGameModularGuiProvider.content);
                }
                if(this.IS_DEBUGGING) {
                    if (this.debugKey.isPressed()) {
                        // Add debug code here
                    }
                }
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        try {
            // This event isn't bound to the Hypixel Network
            if(eventArgs.modID.equals(MODID)){
                this.CONFIG.syncConfig();;
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
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


    public Logger getLOGGER() {
        return LOGGER;
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
