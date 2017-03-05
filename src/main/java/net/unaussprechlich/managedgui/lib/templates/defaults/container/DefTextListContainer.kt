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
 * DefTextListContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 */
open class DefTextListContainer(textList: ArrayList<String>) : Container() {

    private var textList = ArrayList<String>()

    open fun addEntry(s: String) {
        textList.add(s)
        updateSize()
    }

    fun setTextList(textList: ArrayList<String>) {
        this.textList = textList
        updateSize()
    }

    open val listSize: Int
        get() {
            if (textList.isEmpty()) return 0
            return textList.size
        }

    open fun clearAll() {
        textList.clear()
    }

    open fun removeEntry(s: String) {
        textList.removeAll(textList.toList()
                .filter({ s1 -> s1.equals(s, ignoreCase = true) })
        )
    }

    protected open fun updateSize() {
        super.setWidth(FontUtil.getStringWidth(textList.max()!!))
        super.setHeight(textList.size * ConstantsMG.TEXT_Y_OFFSET)
    }

    init {
        this.textList = textList
        updateSize()
    }

    private fun render(xStart: Int, yStart: Int) {
        var yStart = yStart
        for (s in textList) {
            FontUtil.draw(s, xStart, yStart)
            yStart += ConstantsMG.TEXT_Y_OFFSET
        }
    }

    protected fun setWidthLocal(width: Int) {
        super.setWidth(width)
    }

    protected fun setHeightLocal(height: Int) {
        super.setHeight(height)
    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees == EnumEventState.POST) {
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

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
