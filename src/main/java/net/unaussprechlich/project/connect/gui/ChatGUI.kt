package net.unaussprechlich.project.connect.gui

import com.palechip.hudpixelmod.ChatDetector
import com.palechip.hudpixelmod.HudPixelMod
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.hypixel.helper.HypixelRank
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.databases.Player.PlayerModel
import net.unaussprechlich.managedgui.lib.databases.Player.data.Rank
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import java.util.*

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 */
object ChatGUI : GUI() {

    private val tabManager = TabManager()

    private val WIDTH = 500
    private val HEIGHT = 200

    private val scrollSpacerRenderer = object : IScrollSpacerRenderer {
        override fun render(xStart: Int, yStart: Int, width: Int) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, ConstantsMG.DEF_BACKGROUND_RGBA, ColorRGBA(30, 30, 30, 255))
        }
        override val spacerHeight: Int
            get() = 2
    }

    private val scrollALL = DefScrollableContainer(ConstantsMG.DEF_BACKGROUND_RGBA, WIDTH, HEIGHT - 17, scrollSpacerRenderer)
    private val partyCon = DefScrollableContainer(ConstantsMG.DEF_BACKGROUND_RGBA, WIDTH, HEIGHT - 17, scrollSpacerRenderer)
    private val guildCon = DefScrollableContainer(ConstantsMG.DEF_BACKGROUND_RGBA, WIDTH, HEIGHT - 17, scrollSpacerRenderer)
    private val privateCon = DefScrollableContainer(ConstantsMG.DEF_BACKGROUND_RGBA, WIDTH, HEIGHT - 17, scrollSpacerRenderer)

    init {
        registerChild(tabManager)
        tabManager.registerTab(TabContainer(TabListElementContainer("ALL", RGBA.WHITE.get(), tabManager), scrollALL, tabManager))
        tabManager.registerTab(TabContainer(TabListElementContainer("PARTY", RGBA.BLUE.get(), tabManager), partyCon, tabManager))
        tabManager.registerTab(TabContainer(TabListElementContainer("GUILD", RGBA.GREEN.get(), tabManager), guildCon, tabManager))
        tabManager.registerTab(TabContainer(TabListElementContainer("PRIVATE", RGBA.PURPLE_DARK_MC.get(), tabManager), privateCon, tabManager))

        updatePosition()

        ChatDetector.registerEventHandler(ChatDetector.PrivateMessage) {
            addChatMessage(privateCon, it.data["name"].toString(), it.data["message"].toString(), HypixelRank.getRankByName(it.data["rank"].toString()))
        }


        ChatDetector.registerEventHandler(ChatDetector.GuildChat) {
            addChatMessage(guildCon, it.data["name"].toString(), it.data["message"].toString(), HypixelRank.getRankByName(it.data["rank"].toString()))
        }

        ChatDetector.registerEventHandler(ChatDetector.PartyChat) {
            addChatMessage(privateCon, it.data["name"].toString(), it.data["message"].toString(), HypixelRank.getRankByName(it.data["rank"].toString()))
        }

    }


    fun addChatMessage(con: DefScrollableContainer, name: String, message: String, rank: Rank) {
        if (!con.scrollElements.isEmpty() && con.scrollElements[con.scrollElements.size - 1] is DefChatMessageContainer) {
            val conMes = con.scrollElements[con.scrollElements.size - 1] as DefChatMessageContainer
            if (conMes.playername.equals(name, ignoreCase = true)) {
                conMes.addMessage(message)
                return
            }
        }
        con.registerScrollElement(
                DefChatMessageContainer(
                        PlayerModel(name, UUID.randomUUID(), rank, ResourceLocation(HudPixelMod.MODID, "textures/skins/SteveHead.png")),
                        message,
                        DefPictureContainer(),
                        WIDTH
                )
        )
    }

    private fun updatePosition() {
        tabManager.xOffset = 5
        tabManager.yOffset = DisplayUtil.scaledMcHeight - HEIGHT - 17 - 30
    }


    override fun doClientTick(): Boolean {
        return true
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        //GL11.glColor3f(1f, 1f, 1f)
        //GuiInventory.drawEntityOnScreen(1000, 500, 100, MouseHandler.getmX(), MouseHandler.getmY(), Minecraft.getMinecraft().thePlayer);
        return true
    }

    override fun doChatMessage(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doMouseMove(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun doScroll(i: Int): Boolean {
        return true
    }

    override fun doClick(clickType: MouseHandler.ClickType): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        if (event.id == EnumDefaultEvents.SCREEN_RESIZE.get()) {
            updatePosition()
        }
        return true
    }

    override fun doOpenGUI(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResize(): Boolean {
        return true
    }


}
