package eladkay.hudpixel

import eladkay.hudpixel.config.CCategory
import eladkay.hudpixel.config.ConfigPropertyBoolean
import eladkay.hudpixel.modulargui.IHudPixelModularGuiProviderBase
import eladkay.hudpixel.modulargui.ModularGuiHelper
import eladkay.hudpixel.modulargui.components.TimerModularGuiProvider
import eladkay.hudpixel.util.ChatMessageComposer
import eladkay.hudpixel.util.GameType
import eladkay.hudpixel.util.ScoreboardReader
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler
import net.unaussprechlich.hudpixelextended.util.LoggerHelper
import java.util.regex.Pattern

object GameDetector {

    private var cooldown = 0
    private var schedule = false
    private var scheduleWhereami = -1

    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    fun onServerChange(event: net.minecraftforge.event.entity.EntityJoinWorldEvent) {
        if (event.entity !is net.minecraft.client.entity.EntityPlayerSP || !eladkay.hudpixel.GameDetector.enabled) return
        val player = event.entity as net.minecraft.client.entity.EntityPlayerSP
        player.sendChatMessage("/whereami")
        eladkay.hudpixel.GameDetector.cooldown = 5
    }


    @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
    fun onLogin(event: net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent) {
        eladkay.hudpixel.GameDetector.scheduleWhereami = 10
    }

    @JvmStatic
    fun update(st: String) {

        // Get the player in a way which is unlikely to change between Minecraft versions.
        val player = FMLClientHandler.instance().clientPlayerEntity;

        val s = st.toLowerCase()
        if (s.equals("hypixel", ignoreCase = true)) {
            eladkay.hudpixel.GameDetector.currentGameType = eladkay.hudpixel.util.GameType.UNKNOWN //main lobby
        } else if (s.equals("hypixel.net", ignoreCase = true)) {
            eladkay.hudpixel.util.ScoreboardReader.resetCache()
            eladkay.hudpixel.GameDetector.schedule = true
        } else if (s.equals("", ignoreCase = true) || s.equals(" ", ignoreCase = true)) {
            eladkay.hudpixel.GameDetector.schedule = true
        } else if (s.equals(" smash heroes", ignoreCase = true) || s.equals("smash heroes", ignoreCase = true)) {
            eladkay.hudpixel.GameDetector.currentGameType = eladkay.hudpixel.util.GameType.SMASH_HEROES
        } else {
            eladkay.hudpixel.GameDetector.schedule = false

            for (type in eladkay.hudpixel.util.GameType.values()) {
                if (s.toLowerCase().replace(" ", "").contains(type.scoreboardName.toLowerCase().replace(" ", ""))) {
                    if (player == null || player.isDead || type === eladkay.hudpixel.GameDetector.currentGameType) return
                    eladkay.hudpixel.GameDetector.currentGameType = type
                    //success!
                    eladkay.hudpixel.modulargui.ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::setupNewGame)
                    ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameStart)
                    if (HudPixelMod.IS_DEBUGGING)
                        ChatMessageComposer("Changed server! Game is now " + currentGameType).send();
                    isLobby = false
                    return
                }
            }

            if (player != null && !player.isDead) {
                currentGameType = GameType.UNKNOWN
                schedule = true
            }
            cooldown = -1
        }
    }

    private fun notifyModularGuiLib() {

    }

    internal var tick = 0

    @SubscribeEvent
    fun tickly(event: TickEvent.ClientTickEvent) {
        tick++
        if (tick < 19) return
        tick = 0

        if (!enabled) return

        // Get the player in a way which is unlikely to change between Minecraft versions.
        val player = FMLClientHandler.instance().clientPlayerEntity;

        if (player == null) return
        if (player.isDead) return
        var title = ScoreboardReader.getScoreboardTitle()
        title = stripColor(title)!!.toLowerCase()
        cooldown--
        scheduleWhereami--
        if (schedule || cooldown == 0) update(title)
        if (scheduleWhereami == 0 && player != null) {
            scheduleWhereami = -1
            player.sendChatMessage("/whereami")
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ClientChatReceivedEvent) {
        if (!enabled) return
        val message = event.message.unformattedText
        if (message.equals("The game starts in 1 second!", ignoreCase = true)) {
            LoggerHelper.logInfo("[GameDetector]the game with gamemode " + currentGameType?.nm + " has started!")
            HudPixelExtendedEventHandler.onGameStart()
            gameHasntBegan = false
            TimerModularGuiProvider.instance.onGameStart()
        }
        if (message.equals("                            Reward Summary", ignoreCase = true)) {
            LoggerHelper.logInfo("[GameDetector]the game with gamemode " + currentGameType?.nm + " has ended!")
            HudPixelExtendedEventHandler.onGameEnd()
            ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameEnd)
            gameHasntBegan = true
        }
        if (message.toLowerCase().contains("currently on server".toLowerCase())) {
            if (LOBBY_MATCHER.asPredicate().test(message)) { //lobby
                isLobby = true
                gameHasntBegan = true
                ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameEnd)
            }
            event.isCanceled = true
        }
    }

    val LOBBY_MATCHER = Pattern.compile("\\w*lobby\\d+")
    val COLOR_CHAR = '\u00A7'
    private val STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR.toString() + "[0-9A-FK-OR]")
    @ConfigPropertyBoolean(category = CCategory.HUDPIXEL, id = "gameDetector", def = true, comment = "Disable game detector (Risky!)")
    @JvmStatic
    var enabled = true
    @JvmStatic
    var currentGameType: GameType? = GameType.UNKNOWN
        private set
    @JvmStatic
    private var isLobby = false
    @JvmStatic
    var gameHasntBegan = true

    private val instance = GameDetector

    init {
        MinecraftForge.EVENT_BUS.register(instance)
    }

    @JvmStatic
    fun doesGameTypeMatchWithCurrent(type: GameType): Boolean {
        when (type) {
            GameType.UNKNOWN -> return currentGameType === GameType.UNKNOWN
            GameType.ALL_GAMES -> return true

            GameType.QUAKECRAFT -> return currentGameType === GameType.QUAKECRAFT

            GameType.THE_WALLS -> return currentGameType === GameType.THE_WALLS

            GameType.PAINTBALL -> return currentGameType === GameType.PAINTBALL

            GameType.BLITZ -> return currentGameType === GameType.BLITZ

            GameType.TNT_GAMES, GameType.BOW_SPLEEF, GameType.TNT_RUN, GameType.TNT_WIZARDS, GameType.TNT_TAG, GameType.ANY_TNT -> return currentGameType === GameType.ANY_TNT

            GameType.VAMPIREZ -> return currentGameType === GameType.VAMPIREZ

            GameType.MEGA_WALLS -> return currentGameType === GameType.MEGA_WALLS

            GameType.ARENA -> return currentGameType === GameType.ARENA

            GameType.UHC -> return currentGameType === GameType.UHC

            GameType.COPS_AND_CRIMS -> return currentGameType === GameType.COPS_AND_CRIMS

            GameType.WARLORDS -> return currentGameType === GameType.WARLORDS

            GameType.ARCADE_GAMES, GameType.BLOCKING_DEAD, GameType.BOUNTY_HUNTERS, GameType.BUILD_BATTLE, GameType.CREEPER_ATTACK, GameType.DRAGON_WARS, GameType.ENDER_SPLEEF, GameType.FARM_HUNT, GameType.GALAXY_WARS, GameType.PARTY_GAMES_1, GameType.PARTY_GAMES_2, GameType.TRHOW_OUT, GameType.ANY_ARCADE, GameType.FOOTBALL -> return currentGameType === GameType.ANY_ARCADE

            GameType.SPEED_UHC -> return currentGameType === GameType.SPEED_UHC

            GameType.CRAZY_WALLS -> return currentGameType === GameType.CRAZY_WALLS

            GameType.SMASH_HEROES, GameType.SMASH_HEROES_WOSPACE -> return currentGameType === GameType.SMASH_HEROES || currentGameType === GameType.SMASH_HEROES_WOSPACE

            GameType.SKYWARS -> return currentGameType === GameType.SKYWARS

            else -> return false
        }
    }

    @JvmName("getIsLobby")
    @JvmStatic
    fun isLobby(): Boolean {
        return isLobby || gameHasntBegan
    }

    @JvmStatic
    fun stripColor(input: String?): String? {
        if (input == null)
            return null
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("")
    }

    @JvmStatic
    fun shouldRenderStuff(): Boolean {
        return !gameHasntBegan && currentGameType != null
    }

    @JvmStatic
    fun reset() {
        instance.scheduleWhereami = 10
        instance.schedule = true
        instance.cooldown = 10
    }

}
