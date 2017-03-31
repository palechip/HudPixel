package eladkay.hudpixel

import com.google.common.collect.Lists
import eladkay.hudpixel.chat.WarlordsDamageChatFilter
import eladkay.hudpixel.command.*
import eladkay.hudpixel.config.EasyConfigHandler
import eladkay.hudpixel.config.HudPixelConfigGui
import eladkay.hudpixel.modulargui.ModularGuiHelper
import eladkay.hudpixel.modulargui.modules.PlayGameModularGuiProvider
import eladkay.hudpixel.util.*
import eladkay.modulargui.lib.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.GameSettings
import net.minecraft.client.settings.KeyBinding
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.unaussprechlich.hudpixelextended.HudPixelExtended
import net.unaussprechlich.hudpixelextended.update.UpdateNotifier
import net.unaussprechlich.project.connect.Connect
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard
import java.util.*


@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.SHORT_VERSION, name = HudPixelMod.NAME, guiFactory = "eladkay.hudpixel.config.HudPixelGuiFactory", acceptedMinecraftVersions = "1.8.9")
public class HudPixelMod {
    lateinit var gameDetector: GameDetector
    var logger: Logger? = null
        private set
    private var debugKey: KeyBinding? = null // A key used to bind some debugging functionality.
    private var pressToPlay: KeyBinding? = null
    private var openConfigGui: KeyBinding? = null
    private var warlordsChatFilter: WarlordsDamageChatFilter? = null

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        try {
            instance = this
            CONFIG = Configuration(event.suggestedConfigurationFile)
            HudPixelMod.CONFIG.load()

            ClientCommandHandler.instance.registerCommand(GameCommand())
            ClientCommandHandler.instance.registerCommand(ScoreboardCommand())
            ClientCommandHandler.instance.registerCommand(GameDetectorCommand())
            ClientCommandHandler.instance.registerCommand(BookVerboseInfoCommand())
            ClientCommandHandler.instance.registerCommand(DiscordCommand)
            ClientCommandHandler.instance.registerCommand(NameCommand)
            ClientCommandHandler.instance.registerCommand(VerboseChatOutputCommand)
            ClientCommandHandler.instance.registerCommand(ClickEventCommand)
            ClientCommandHandler.instance.registerCommand(AfkCommand)
            ClientCommandHandler.instance.registerCommand(ManagedGuiDisableCommand)

            ChatDetector
            HudPixelMethodHandles()
            FireAntiRenderer()
            BetterGgHandler.init()
            // Initialize the logger
            this.logger = LogManager.getLogger("HudPixel")

            // load the configuration file
            EasyConfigHandler.init(event.asmData)

            try {
                val config = Class.forName("Config")
                val field = config.getDeclaredField("gameSettings")
                val gameSettings = field.get(null) as GameSettings
                val fastRenderField = GameSettings::class.java.getDeclaredField("ofFastRender")
                fastRenderField.isAccessible = true
                fastRenderField.set(gameSettings, false)
            } catch (throwable: Throwable) {
                //NO-OP
            }

            //public static boolean isFastRender() { return gameSettings.ofFastRender; }

        } catch (e: Exception) {
            this.logWarn("An exception occured in preInit(). Stacktrace below.")
            e.printStackTrace()
        }

    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {

        MinecraftForge.EVENT_BUS.register(this)
        FMLCommonHandler.instance().bus().register(this)
        MinecraftForge.EVENT_BUS.register(Renderer())
        MinecraftForge.EVENT_BUS.register(ModularGuiHelper())
        ModularGuiHelper.init()
        ContributorFancinessHandler.init()
        BansHandler.init()
        KeyTracker


        // setup HudPixelExtended
        HudPixelExtended.getInstance().setup()

        // initialize createModList
        this.gameDetector = GameDetector
        this.warlordsChatFilter = WarlordsDamageChatFilter()

        // Initialize key bindings
        this.pressToPlay = KeyBinding("Press this key to play the game set in the Modular GUI", Keyboard.KEY_P, KEY_CATEGORY)
        this.openConfigGui = KeyBinding("Open Config", Keyboard.KEY_M, KEY_CATEGORY)
        ClientRegistry.registerKeyBinding(this.pressToPlay)
        ClientRegistry.registerKeyBinding(this.openConfigGui)
        if (IS_DEBUGGING) {
            this.debugKey = KeyBinding("DEBUG KEY", Keyboard.KEY_J, KEY_CATEGORY)
            ClientRegistry.registerKeyBinding(this.debugKey)
        }
    }

    @EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        HudPixelExtended.getInstance().setupPOST()
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatMessage(event: ClientChatReceivedEvent) {
        try {
            if (isHypixelNetwork) {
                if (event.type.toInt() == 0) { // this one reads the normal chat messages
                    this.warlordsChatFilter!!.onChat(event)
                }
            }
        } catch (e: Exception) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.")
            e.printStackTrace()
        }

    }

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent) {
        try {
            BansHandler.checkForBan()
            // Don't do anything unless we are on Hypixel
            if (isHypixelNetwork) {
                // make sure the Scoreboard reader updates when necessary
                ScoreboardReader.resetCache()

                //Send info to remote server
                //NOTE: THIS DOES NOT SEND ANY SESSION KEYS OR PERSONALLY IDENTIFIER INFORMATION!
                if (!didTheThings && Minecraft.getMinecraft().thePlayer != null) {

                    Connect.setup()

                    createModList()
                    var s = ""
                    for (st in modlist) s += st.replace(" ", "-") + ","
                    WebUtil.sendGet("HudPixelMod", IP + "?username=" + Minecraft.getMinecraft().thePlayer.name +
                            "&modlist=" + s + "&timestamp=" + Date().toString().replace(" ", "") + "&uuid=" +
                            Minecraft.getMinecraft().thePlayer.gameProfile.id + "&version=" + DEFAULT_VERSION.replace(" ", ""))
                    didTheThings = true
                }
            }
        } catch (e: Exception) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.")
            e.printStackTrace()
        }

    }


    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent) {
        try {
            // Don't do anything unless we are on Hypixel
            if (isHypixelNetwork) {
                if (this.openConfigGui!!.isPressed) {
                    // open the config screen
                    FMLClientHandler.instance().client.displayGuiScreen(HudPixelConfigGui(null))
                } else if (this.pressToPlay!!.isPressed) {
                    // open the config screen
                    FMLClientHandler.instance().client.thePlayer.sendChatMessage("/play " + PlayGameModularGuiProvider.content)
                } else if (IS_DEBUGGING) {
                    if (this.debugKey!!.isPressed) {
                        // Add debug code here
                    }
                }
            }
        } catch (e: Exception) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.")
            e.printStackTrace()
        }

    }

    fun logDebug(s: String) {
        if (IS_DEBUGGING) {
            this.logger!!.info("[DEBUG] " + s)
        }
    }

    fun logInfo(s: String) {
        this.logger!!.info(s)
    }

    fun logWarn(s: String) {
        this.logger!!.warn(s)
    }

    fun logError(s: String) {
        this.logger!!.error(s)
    }

    companion object {

        const val MODID = "hudpixel"
        const val SHORT_VERSION = "3.0" // only to be used for the annotation which requires such a constant.
        const val DEFAULT_VERSION = "3.3dev"
        const val HYPIXEL_DOMAIN = "hypixel.net"
        const internal val NAME = "HudPixel Reloaded"
        // key related vars
        private val KEY_CATEGORY = "HudPixel Mod"
        private val IP = "http://hudpixel.unaussprechlich.net/HudPixel/files/hudpixelcallback.php" //moved the database ;)
        lateinit var CONFIG: Configuration
        var isUpdateNotifierDone = false
        private val devEnvOverride = false //if this is true, the environment will launch as normal, even in a

        //dev environment
        val IS_DEBUGGING = Launch.blackboard["fml.deobfuscatedEnvironment"]!!.asBoolean() && !devEnvOverride

        private var instance: HudPixelMod? = null
        private val modlist = Lists.newArrayList<String>()
        private var didTheThings = false

        /**
         * Checks if the player is on Hypixel Network.
         */
        // get the IP of the current server
        // only if there is one
        // Did the player disconnect?
        // if the server ip ends with hypixel.net, it belongs to the Hypixel Network (mc.hypixel.net, test.hypixel.net, mvp.hypixel.net, creative.hypixel.net)
        // it can happen that the server data doesn't get null
        val isHypixelNetwork: Boolean
            get() {
                if (IS_DEBUGGING) {
                    return true
                }
                if (FMLClientHandler.instance().client.currentServerData == null) {
                    instance().logDebug("Disconnected from Hypixel Network")
                    return false
                }
                val ip = FMLClientHandler.instance().client.currentServerData.serverIP
                if (ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {
                    instance().logDebug("Joined Hypixel Network")
                    if (!isUpdateNotifierDone) {
                        UpdateNotifier(true)
                        isUpdateNotifierDone = true
                    }
                    return true
                } else if (!ip.toLowerCase().endsWith(HYPIXEL_DOMAIN.toLowerCase())) {

                    instance().logDebug("Disconnected from Hypixel Network")
                    return false
                }
                return false
            }

        private fun createModList() {
            modlist.clear()
            val b = Loader.instance().activeModList
            for (modContainer in b) {
                val l = modContainer.name
                if (!l.contains("Minecraft Coder Pack") && !l.contains("Forge Mod Loader") && !l.contains("Minecraft Forge"))
                    modlist.add(modContainer.name)
            }
        }

        fun instance(): HudPixelMod {
            return instance!!
        }
    }
}

