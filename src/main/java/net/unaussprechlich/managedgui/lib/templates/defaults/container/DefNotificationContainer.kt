/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RenderUtils

/**
 * DefNotificationContainer Created by unaussprechlich on 30.12.2016.
 * Description:
 */
class DefNotificationContainer(private val message: String, private val title: String, private val color: ColorRGBA, var showtime_sec: Int) : DefBackgroundContainer(ConstantsMG.DEF_BACKGROUND_RGBA, 400, 100) {

    private val titleContainer = DefTextContainer(this.title)
    private val messageContainer = DefTextAutoLineBreakContainer(this.message, width - 10)


    init {
        titleContainer.xOffset = 6
        titleContainer.yOffset = 3

        messageContainer.xOffset = 6
        messageContainer.yOffset = 4 + titleContainer.height

        updateHeight()

        registerChild(titleContainer)
        registerChild(messageContainer)
    }

    private fun updateHeight() {
        height = messageContainer.height + titleContainer.height + 5
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (showtime_sec < 0) return false
        if (ees === EnumEventState.PRE) return true

        RenderUtils.renderBoxWithColor(xStart, yStart, 2, getHeight(), color)

        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if (iEvent.id == EnumDefaultEvents.TIME.get() && iEvent.data === EnumTime.SEC_1) showtime_sec--
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }
}
