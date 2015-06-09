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

import java.util.ArrayList;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.chat.LobbyCommandAutoCompleter;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.config.HudPixelConfigGui;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.stats.BlitzStatsDisplayer;
import com.palechip.hudpixelmod.uptodate.HudPixelDeactivatedException;
import com.palechip.hudpixelmod.uptodate.UpToDateThread;
import com.palechip.hudpixelmod.uptodate.UpdateChannel;
import com.palechip.hudpixelmod.uptodate.UpdateInformation;
import com.palechip.hudpixelmod.uptodate.UpdateNotifier;
import com.palechip.hudpixelmod.uptodate.VersionInformation;
import com.palechip.hudpixelmod.util.ChatMessageComposer;
import com.palechip.hudpixelmod.util.ScoreboardReader;

import cpw.mods.fml.client.FMLClientHandler;
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

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION, name = HudPixelMod.NAME, guiFactory = "com.palechip.hudpixelmod.config.HudPixelGuiFactory")
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String NAME = "HudPixel Reloaded";
    public static final String VERSION = "2.5.0";
    public static final UpdateChannel UPDATE_CHANNEL = UpdateChannel.DEV;
    public static final boolean IS_DEBUGGING = true;

    private static HudPixelMod instance;

    public Logger LOGGER;
    public HudPixelConfig CONFIG;
    public UpdateNotifier  updateNotifier;
    public HudPixelRenderer renderer;
    private Queue apiQueue;
    
    private boolean deactivate = false;
    private VersionInformation deactivationInformation;

    private HypixelNetworkDetector hypixelDetector;
    public GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    // key related vars
    public static final String KEY_CATEGORY = "HudPixel Mod";
    private KeyBinding hideHUDKey;
    private KeyBinding openConfigGui;
    private KeyBinding debugKey; // A key used to bind some debugging functionality

    private LobbyCommandAutoCompleter lobbyCommandConfirmer;
    private WarlordsDamageChatFilter warlordsChatFilter;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            instance = this;
            // create an empty notifier
            this.updateNotifier = new UpdateNotifier(new UpdateInformation());
            
            // start the HudPixel Up To Date Loader
            new UpToDateThread(event.getModConfigurationDirectory());
            // Initialize the logger
            this.LOGGER = LogManager.getLogger("HudPixel");
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
        // register this class as an event handler
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        // initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);
        this.renderer = new HudPixelRenderer(this.updateNotifier);
        this.lobbyCommandConfirmer = new LobbyCommandAutoCompleter();
        this.warlordsChatFilter = new WarlordsDamageChatFilter();

        // Initialize key bindings
        this.hideHUDKey = new KeyBinding("Hide HUD", Keyboard.KEY_F9, KEY_CATEGORY);
        this.openConfigGui = new KeyBinding("Open Config", Keyboard.KEY_P, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(this.hideHUDKey);
        ClientRegistry.registerKeyBinding(this.openConfigGui);
        if(this.IS_DEBUGGING) {
            this.debugKey = new KeyBinding("DEBUG KEY", Keyboard.KEY_J, KEY_CATEGORY);
            ClientRegistry.registerKeyBinding(this.debugKey);
        }
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

                // and the booster display needs it as well
                this.renderer.boosterDisplay.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());

                //auto completion of /lobby
                this.lobbyCommandConfirmer.onChatReceived(event);

                //send event to Warlords damage chat disabler
                // NOT NEEDED IT 1.7.10
                //this.warlordsChatFilter.onChat(event);
            }       
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    /**
     * This is an emergency switch. It will crash the game. Only to be used in case of a severe bug or disallowed features.
     */
    public void invokeDeactivation(VersionInformation versionInformation) {
        this.deactivationInformation = versionInformation;
        this.deactivate = true;
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            // check if HudPixel has to be deactivated
            if(this.deactivate) {
                throw new HudPixelDeactivatedException(this.deactivationInformation);
            }
            
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

                this.renderer.boosterDisplay.onClientTick();

                // check if the update message can be displayed
                this.updateNotifier.onTick();
            }
        } catch (HudPixelDeactivatedException e) {
            // let the game crash with this exception
            throw e;
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        try {
            // Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {
                // render the game
                this.renderer.onRenderTick();
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
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
                this.CONFIG.syncConfig();
                // reload stuff that uses the config values for immediate effect
                this.renderer.loadRenderingProperties(updateNotifier);
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onInitGui(InitGuiEvent event) {
        try {
            // Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {
                // pass the event to the booster display
                // used to inject the tip-all button into GuiChat
                this.renderer.boosterDisplay.onInitGui(event);
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void onGuiActionPerformed(ActionPerformedEvent event) {
        try {
            // Don't do anything unless we are on Hypixel
            if(this.hypixelDetector.isHypixelNetwork) {
                // notify all classes which have registered a button using on init gui
                this.renderer.boosterDisplay.onGuiActionPerformed(event);
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
