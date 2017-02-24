/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package com.palechip.hudpixelmod;

import com.google.common.collect.Lists;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;
import com.palechip.hudpixelmod.command.*;
import com.palechip.hudpixelmod.config.EasyConfigHandler;
import com.palechip.hudpixelmod.config.HudPixelConfigGui;
import com.palechip.hudpixelmod.extended.HudPixelExtended;
import com.palechip.hudpixelmod.extended.update.UpdateNotifier;
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper;
import com.palechip.hudpixelmod.modulargui.modules.PlayGameModularGuiProvider;
import com.palechip.hudpixelmod.util.*;
import eladkay.modulargui.lib.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.unaussprechlich.project.connect.Connect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static com.palechip.hudpixelmod.HudPixelMod.SHORT_VERSION;

@Mod(
        modid = HudPixelMod.MODID,
        version = SHORT_VERSION,
        name = HudPixelMod.NAME,
        guiFactory = "com.palechip.hudpixelmod.config.HudPixelGuiFactory",
        acceptedMinecraftVersions = "1.8.9"
)
public class HudPixelMod {

    public static final String MODID = "hudpixel";
    public static final String SHORT_VERSION = "3.0"; // only to be used for the annotation which requires such a constant.
    public static final String DEFAULT_VERSION = "3.3dev";
    public static final String HYPIXEL_DOMAIN = "hypixel.net";
    static final String NAME = "HudPixel Reloaded";
    // key related vars
    private static final String KEY_CATEGORY = "HudPixel Mod";
    private static final String IP = "http://hudpixel.unaussprechlich.net/HudPixel/files/hudpixelcallback.php"; //moved the database ;)
    public static Configuration CONFIG;
    public static boolean isUpdateNotifierDone = false;
    private static boolean devEnvOverride = false; //if this is true, the environment will launch as normal, even in a

    //dev environment
    public static final boolean IS_DEBUGGING = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") && !devEnvOverride;

    private static HudPixelMod instance;
    private static List<String> modlist = Lists.newArrayList();
    private static boolean didTheThings = false;
    public GameDetector gameDetector;
    private Logger LOGGER;
    private KeyBinding debugKey; // A key used to bind some debugging functionality.
    private KeyBinding pressToPlay;
    private KeyBinding openConfigGui;
    private WarlordsDamageChatFilter warlordsChatFilter;

    /**
     * Checks if the Player is on Hypixel Network.
     */
    public static boolean isHypixelNetwork() {
        if (IS_DEBUGGING) {
            return true;
        }
        // get the IP of the current server
        // only if there is one
        if (FMLClientHandler.instance().getClient().getCurrentServerData() == null) {
            // Did the player disconnect?
            instance().logDebug("Disconnected from Hypixel Network");
            return false;
        }
        String ip = FMLClientHandler.instance().getClient().getCurrentServerData().serverIP;
        // if the server ip ends with hypixel.net, it belongs to the Hypixel Network (mc.hypixel.net, test.hypixel.net, mvp.hypixel.net, creative.hypixel.net)
        if (ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {
            instance().logDebug("Joined Hypixel Network");
            if (!isUpdateNotifierDone) {
                new UpdateNotifier(true);
                isUpdateNotifierDone = true;
            }
            return true;
        }
        // it can happen that the server data doesn't get null
        else if (!ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {

            instance().logDebug("Disconnected from Hypixel Network");
            return false;
        }
        return false;
    }

    private static void createModList() {
        List<ModContainer> b = Loader.instance().getActiveModList();
        for (ModContainer modContainer : b) {
            String l = modContainer.getName();
            if (!l.contains("Minecraft Coder Pack") && !l.contains("Forge Mod Loader") && !l.contains("Minecraft Forge"))
                modlist.add(modContainer.getName());
        }
    }

    public static HudPixelMod instance() {
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            instance = this;
            CONFIG = new Configuration(event.getSuggestedConfigurationFile());
            HudPixelMod.CONFIG.load();

            ClientCommandHandler.instance.registerCommand(new GameCommand());
            ClientCommandHandler.instance.registerCommand(new ScoreboardCommand());
            ClientCommandHandler.instance.registerCommand(new GameDetectorCommand());
            ClientCommandHandler.instance.registerCommand(new BookVerboseInfoCommand());
            ClientCommandHandler.instance.registerCommand(DiscordCommand.INSTANCE);
            ClientCommandHandler.instance.registerCommand(NameCommand.INSTANCE);
            ClientCommandHandler.instance.registerCommand(VerboseChatOutputCommand.INSTANCE);
            ClientCommandHandler.instance.registerCommand(ClickEventCommand.INSTANCE);
            ClientCommandHandler.instance.registerCommand(AfkCommand.INSTANCE);
            new HudPixelMethodHandles();
            new FireAntiRenderer();
            BetterGgHandler.init();
            // Initialize the logger
            this.LOGGER = LogManager.getLogger("HudPixel");

            // load the configuration file
            EasyConfigHandler.INSTANCE.init(event.getAsmData());

            try {
                Class config = Class.forName("Config");
                Field field = config.getDeclaredField("gameSettings");
                GameSettings gameSettings = (GameSettings) field.get(null);
                Field fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
                fastRenderField.setAccessible(true);
                fastRenderField.set(gameSettings, false);
            } catch(Throwable throwable) {
                //NO-OP
            }

            //public static boolean isFastRender() { return gameSettings.ofFastRender; }

        } catch (Exception e) {
            this.logWarn("An exception occured in preInit(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(new Renderer());
        MinecraftForge.EVENT_BUS.register(new ModularGuiHelper());
        ModularGuiHelper.init();
        ContributorFancinessHandler.init();
        BansHandler.init();


        // setup HudPixelExtended
        HudPixelExtended.getInstance().setup();

        // initialize createModList
        this.gameDetector = new GameDetector();
        this.warlordsChatFilter = new WarlordsDamageChatFilter();

        // Initialize key bindings
        this.pressToPlay = new KeyBinding("Press this key to play the game set in the Modular GUI", Keyboard.KEY_P, KEY_CATEGORY);
        this.openConfigGui = new KeyBinding("Open Config", Keyboard.KEY_M, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(this.pressToPlay);
        ClientRegistry.registerKeyBinding(this.openConfigGui);
        if (IS_DEBUGGING) {
            this.debugKey = new KeyBinding("DEBUG KEY", Keyboard.KEY_J, KEY_CATEGORY);
            ClientRegistry.registerKeyBinding(this.debugKey);
        }
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event){
        HudPixelExtended.getInstance().setupPOST();
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent event) {
        try {
            if (isHypixelNetwork()) {
                if (event.type == 0) { // this one reads the normal chat messages
                    this.warlordsChatFilter.onChat(event);
                }
            }
        } catch (Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            BansHandler.checkForBan();
            // Don't do anything unless we are on Hypixel
            if (isHypixelNetwork()) {
                // make sure the Scoreboard reader updates when necessary
                ScoreboardReader.resetCache();

                //Send info to remote server
                //NOTE: THIS DOES NOT SEND ANY SESSION KEYS OR PERSONALLY IDENTIFIER INFORMATION!
                if (!didTheThings && Minecraft.getMinecraft().thePlayer != null) {

                    Connect.getINSTANCE().setup();

                    createModList();
                    String s = "";
                    for (String st : modlist) s += st.replace(" ", "-") + ",";
                    WebUtil.sendGet("HudPixelMod", IP + "?username=" + Minecraft.getMinecraft().thePlayer.getName() +
                            "&modlist=" + s + "&timestamp=" + new Date().toString().replace(" ", "") + "&uuid=" +
                            Minecraft.getMinecraft().thePlayer.getGameProfile().getId() + "&version=" + DEFAULT_VERSION.replace(" ", ""));
                    didTheThings = true;
                }
            }
        } catch (Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }


    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        try {
            // Don't do anything unless we are on Hypixel
            if (isHypixelNetwork()) {
                if (this.openConfigGui.isPressed()) {
                    // open the config screen
                    FMLClientHandler.instance().getClient().displayGuiScreen(new HudPixelConfigGui(null));
                } else if (this.pressToPlay.isPressed()) {
                    // open the config screen
                    FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/play " + PlayGameModularGuiProvider.content);
                } else if (IS_DEBUGGING) {
                    if (this.debugKey.isPressed()) {
                        // Add debug code here
                    }
                }
            }
        } catch (Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    public void logDebug(String s) {
        if (IS_DEBUGGING) {
            this.LOGGER.info("[DEBUG] " + s);
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

