/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefBackgroundContainer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import java.util.*

/**
 * TabManager Created by Alexander on 24.02.2017.
 * Description:
 */
class TabManager : Container() {

    private val tabs = ArrayList<TabContainer>()
    private val moveCon = DefBackgroundContainer(RGBA.P1B1_596068.get(), 16, 16)

    private var activeTab: TabContainer? = null

    private var move = false

    init {
        registerChild(moveCon)
        moveCon.registerClickedListener { clickType, container ->
            if (clickType == MouseHandler.ClickType.DRAG)
                move = true

            if (clickType == MouseHandler.ClickType.DROP && move)
                move = false
        }
    }

    fun setTabActive(tab: TabContainer) {
        if (activeTab === tab) return
        if (activeTab != null) activeTab!!.setClosed()
        activeTab = tab
        activeTab!!.setOpen()
    }


    fun registerTab(tab: TabContainer) {
        if (activeTab == null) setTabActive(tab)
        registerChild(tab)
        tabs.add(tab)
        updatePositions()
    }

    fun unregisterTab(tab: TabContainer) {
        unregisterChild(tab)
        tabs.remove(tab)
        updatePositions()
    }

    private fun updatePositions() {
        var offset = 17
        for (element in tabs.map{it -> it.tabListElement}.toList()){
            element.xOffset = offset
            offset += element.width
        }
    }

    override fun doClientTickLocal(): Boolean {
        if (move) {
            setXYOffset(MouseHandler.mX - 8, MouseHandler.mY - 8)
        }
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
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
