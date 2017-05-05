package net.unaussprechlich.project.connect.gui

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.project.connect.chat.ChatWrapper

object TestGUI : GUI(){

    init {
        registerChild(ChatWrapper)
    }

    fun update(){

    }

    override fun doClientTick(): Boolean { update() ; return true }
    override fun doRender(xStart: Int, yStart: Int): Boolean { return true }
    override fun doChatMessage(e: ClientChatReceivedEvent?): Boolean { return true }
    override fun doMouseMove(mX: Int, mY: Int): Boolean { return true }
    override fun doScroll(i: Int): Boolean { return true }
    override fun doClick(clickType: MouseHandler.ClickType?): Boolean { return true }
    override fun <T : Event<*>?> doEventBus(event: T): Boolean { return true }
    override fun doOpenGUI(e: GuiOpenEvent?): Boolean { return true }
    override fun doResize(): Boolean { return true }

}


