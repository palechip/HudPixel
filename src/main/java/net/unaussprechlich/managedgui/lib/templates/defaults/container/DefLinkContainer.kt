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
import net.unaussprechlich.managedgui.lib.util.ColorRGBA
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.Utils.openWebLink


class DefLinkContainer(text : String, var link : String, var color : ColorRGBA = RGBA.BLUE_MC.get(), var colorHoover : ColorRGBA = RGBA.WHITE.get()) : Container() {

    var text = ""
        set(value) {
            field = value
            width = FontUtil.getStringWidth(value)
        }

    init {
        this.text = text
        height = 9
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState?): Boolean {
        if(ees == EnumEventState.PRE) return true

        if(!isHover) FontUtil.drawWithColor(text, xStart, yStart, color)
        else         FontUtil.drawWithColor(text, xStart, yStart, colorHoover)

        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType?, isThisContainer: Boolean): Boolean {
        if(isHover && clickType == MouseHandler.ClickType.SINGLE) openWebLink(link)
        return true
    }


    override fun doClientTickLocal(): Boolean { return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent?): Boolean { return true }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun <T : Event<*>?> doEventBusLocal(iEvent: T): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent?): Boolean { return true }
    override fun doResizeLocal(width: Int, height: Int): Boolean { return true }


}