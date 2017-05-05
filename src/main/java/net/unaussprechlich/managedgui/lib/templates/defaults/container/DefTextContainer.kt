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
import net.unaussprechlich.managedgui.lib.util.FontUtil

/**
 * DefTextContainer Created by unaussprechlich on 20.12.2016.
 * Description:
 */
open class DefTextContainer(text: String) : Container() {

    operator fun plusAssign(char: Char) {
        text += char
    }

    var text = ""
        set(text) {
            field = text
            super.setWidth(FontUtil.getStringWidth(text))
            super.setHeight(9)
        }
    var isShadow = false

    init {
        this.text = text
        super.setWidth(FontUtil.getStringWidth(text))
        super.setHeight(9)
    }

    override fun setWidth(width: Int) {
        throw UnsupportedOperationException("[ManagedGuiLib][DefTextContainer] setWidth() is handled automatically use setPadding() instead!")
    }

    override fun setHeight(width: Int) {
        throw UnsupportedOperationException("[ManagedGuiLib][DefTextContainer] setHeight() is handled automatically use setPadding() instead!")
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees === EnumEventState.PRE) {
            FontUtil.draw(this.text, xStart, yStart)
        }
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

    override fun <T : Event<*>> doEventBusLocal(e: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
