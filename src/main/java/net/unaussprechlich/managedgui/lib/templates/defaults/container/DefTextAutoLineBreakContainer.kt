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
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import java.util.*

/**
 * DefTextAutoLineBreakContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 */
open class DefTextAutoLineBreakContainer(var text: String, width: Int) : Container() {

    protected var renderList: MutableList<String> = ArrayList()
        private set

    private var prevText = ""
    private var prevWidth = 0

    protected open fun onUpdate(){
        update()
    }

    fun update() {
        if (text == prevText && prevWidth == width) return
        prevText = text
        prevWidth = width



        var newRenderList : MutableList<String> = ArrayList()

        if (FontUtil.getStringWidth(this.text) <= this.width)
            newRenderList.add(text)
        else
            newRenderList = FontUtil.fontRenderer.listFormattedStringToWidth(text, width)
        super.setHeight(ConstantsMG.TEXT_Y_OFFSET * renderList.size)

        renderList = newRenderList
        onUpdate()
    }

    init {
        super.setWidth(width)
    }

    private fun render(xStart: Int, yStart: Int) {
        var y = yStart
        for (s in renderList) {
            FontUtil.draw(s, xStart, y)
            y += ConstantsMG.TEXT_Y_OFFSET
        }
    }

    override fun setHeight(width: Int) {
        throw UnsupportedOperationException("[ManagedGuiLib][DefTextAutoLineBreakContainer] setHeight() is handled automatically use setPadding() instead!")
    }

    override fun doClientTickLocal(): Boolean {
        update()
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees === EnumEventState.POST) {
            render(xStart, yStart)
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

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }


    public override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
