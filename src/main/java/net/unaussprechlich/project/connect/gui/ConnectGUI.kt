package net.unaussprechlich.project.connect.gui

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler

/**
 * ConnectGUI Created by unaussprechlich on 20.12.2016.
 * Description:
 */
object ConnectGUI : GUI() {
    init {
        GuiManagerMG.addGUI("NotificationGUI", NotificationGUI)
        //GuiManagerMG.addGUI("ConnectChatGui", ChatGUI)
        GuiManagerMG.addGUI("TestGUI", TestGUI)
    }

    override fun doClientTick(): Boolean {

        return true
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {

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
        if (!(event.id == EnumDefaultEvents.TIME.get() && event.data === EnumTime.SEC_15)) return true
        return true
    }

    override fun doOpenGUI(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResize(): Boolean {
        return true
    }
}
