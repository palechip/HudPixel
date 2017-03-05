package net.unaussprechlich.project.connect.gui

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.gui.GUI
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefNotificationContainer
import java.util.*

/**
 * NotificationGUI Created by Alexander on 23.02.2017.
 * Description:
 */
object NotificationGUI : GUI() {

    private val notifications = ArrayList<DefNotificationContainer>()

    fun addNotification(notify: DefNotificationContainer) {
        notifications.add(notify)
        registerChild(notify)
        updatePositions()
    }

    private fun updatePositions() {
        var yPos = 0
        for (notify in notifications) {
            notify.yOffset = yPos
            yPos += notify.height + SPACING
        }
    }

    override fun doClientTick(): Boolean {
        return true
    }

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        return true
    }

    override fun doChatMessage(e: ClientChatReceivedEvent): Boolean {
        return false
    }

    override fun doMouseMove(mX: Int, mY: Int): Boolean {
        return false
    }

    override fun doScroll(i: Int): Boolean {
        return false
    }

    override fun doClick(clickType: MouseHandler.ClickType): Boolean {
        return false
    }

    override fun <T : Event<*>> doEventBus(event: T): Boolean {
        if (event.id == EnumDefaultEvents.TIME.get() && event.data === EnumTime.SEC_1) {
            notifications.removeAll(notifications.filter({ cont -> cont.showtime_sec <= 0 }))
            updatePositions()
        }
        return true
    }

    override fun doOpenGUI(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResize(): Boolean {
        return true
    }

    private val SPACING = 1

}
