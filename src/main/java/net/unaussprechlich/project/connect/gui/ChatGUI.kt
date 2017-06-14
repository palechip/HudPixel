package net.unaussprechlich.project.connect.gui

import com.mojang.realmsclient.gui.ChatFormatting
import eladkay.hudpixel.ChatDetector
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.unaussprechlich.hypixel.helper.HypixelRank
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefButtonContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.container.ChatScrollContainer
import net.unaussprechlich.project.connect.container.ChatTabContainer
import org.lwjgl.input.Keyboard

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 */
object ChatGUI : GUI() {

    internal val tabManager = TabManager()

    val WIDTH = 500
    val HEIGHT = 200

    private var visible = false

    var privateChatCons : MutableMap<String, ChatTabContainer> = hashMapOf()

    val scrollSpacerRenderer = object : IScrollSpacerRenderer {
        override fun render(xStart: Int, yStart: Int, width: Int) {
            RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 25, yStart, width - 42, 1, RGBA.P1B1_596068.get())
        }
        override val spacerHeight: Int
            get() = 1
    }

    private val allCon = ChatTabContainer(TabListElementContainer("ALL", RGBA.WHITE.get(), tabManager), ChatScrollContainer(), tabManager)
    private val partyCon = ChatTabContainer(TabListElementContainer("PARTY", RGBA.BLUE.get(), tabManager), ChatScrollContainer(), tabManager)
    private val guildCon = ChatTabContainer(TabListElementContainer("GUILD", RGBA.GREEN.get(), tabManager), ChatScrollContainer(), tabManager)

    init {
        tabManager.isVisible = false
        registerChild(tabManager)

        tabManager.registerTab(allCon)
        tabManager.registerTab(partyCon)
        tabManager.registerTab(guildCon)

        updatePosition()

        ChatDetector.registerEventHandler(ChatDetector.PrivateMessage) {
            println(privateChatCons.keys.toString())
            val name = it.data["name"].toString()
            if(!privateChatCons.contains(name))
                openPrivateChat(name)
            privateChatCons[name]!!.addChatMessage(
                    if(it.data["type"] == "From")
                        name
                    else
                        FMLClientHandler.instance().clientPlayerEntity.name ,
                    it.data["message"].toString(),
                    if(it.data["type"] == "From")
                        HypixelRank.getRankByName(it.data["rank"].toString())
                    else
                        Rank("[YOU]", "" + ChatFormatting.YELLOW + "[YOU]", ChatFormatting.YELLOW)
            )
        }

        ChatDetector.registerEventHandler(ChatDetector.GuildChat) {
            println(it.data["name"].toString())
            guildCon.addChatMessage(it.data["name"].toString(), it.data["message"].toString(), HypixelRank.getRankByName(it.data["rank"].toString()))
        }

        ChatDetector.registerEventHandler(ChatDetector.PartyChat) {
            partyCon.addChatMessage(it.data["name"].toString(), it.data["message"].toString(), HypixelRank.getRankByName(it.data["rank"].toString()))
        }

    }

    fun openPrivateChat(user : String) {
        val chatCon = ChatTabContainer(TabListElementContainer(user, RGBA.PURPLE_DARK_MC.get(), tabManager),
                            ChatScrollContainer(),
                            tabManager
                      )
        tabManager.registerTab(chatCon)
        privateChatCons[user] = chatCon

        chatCon.tabListElement.registerButton((DefButtonContainer(7, 7,
            { click, con ->
                if(click == MouseHandler.ClickType.SINGLE)
                    closePrivateChatCon(chatCon.tabListElement.title)
            },{ xStart, yStart, width, height ->
                RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart, yStart , width , height, RGBA.P1B1_596068.get(), RGBA.P1B1_596068.get(), RGBA.P1B1_596068.get(), RGBA.P1B1_596068.get())
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 1, yStart + height - 2, width - 2, 1, RGBA.P1B1_DEF.get())
            },{ xStart, yStart, width, height ->
            RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart, yStart , width , height, RGBA.WHITE.get(), RGBA.WHITE.get(), RGBA.WHITE.get(), RGBA.WHITE.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 1, yStart + height - 2, width - 2, 1, RGBA.P1B1_DEF.get())
            }))
        )
    }

    fun closePrivateChatCon(user : String){
        if(!privateChatCons.containsKey(user)) return
        tabManager.unregisterTab(privateChatCons[user] as TabContainer)
        privateChatCons.remove(user)
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
        } else if (event.id == EnumDefaultEvents.KEY_PRESSED.get()){
            if((event as KeyPressedEvent).data.equals("k" , true)){
                GuiManagerMG.bindScreen()
                tabManager.isVisible = true
                Keyboard.enableRepeatEvents(true)
            }
        }else if (event.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()){
            if((event as KeyPressedCodeEvent).data == Keyboard.KEY_ESCAPE){
                tabManager.isVisible = false
                Keyboard.enableRepeatEvents(false)
            }
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
