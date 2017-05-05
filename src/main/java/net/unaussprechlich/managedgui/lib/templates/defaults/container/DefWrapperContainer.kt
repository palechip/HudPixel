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
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState


open class DefWrapperContainer : Container(){

    override fun doRender(xStart: Int, yStart: Int): Boolean {
        updateXYStart(xStart, yStart)
        if (!isVisible) return false
        if (!doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.PRE)) return false
        return doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.POST)
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState?): Boolean {
        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent?): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType?, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent?): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}