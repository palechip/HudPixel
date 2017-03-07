/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers

import com.palechip.hudpixelmod.extended.util.ImageLoader
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import java.util.*

/**
 * TabManager Created by Alexander on 24.02.2017.
 * Description:
 */
class TabManager : Container() {

    private val tabs = ArrayList<TabContainer>()

    private val BS = 17


    private val minCon = DefPictureContainer(BS, BS, ImageLoader.chatMinimizeLocation())
    private val maxCon = DefPictureContainer(BS, BS, ImageLoader.chatMaximizeLocation())
    private val moveCon = DefPictureContainer(BS, BS, ImageLoader.chatMoveLocation())

    private val addCon =  DefPictureContainer(17, 17, ImageLoader.chatTabAddLocation())

    private var activeTab: TabContainer? = null

    private var move = false

    init {
        registerChild(moveCon)
        registerChild(maxCon)
        registerChild(minCon)
        registerChild(addCon)

        moveCon.apply {
            registerClickedListener { clickType, container ->
                if (clickType == MouseHandler.ClickType.DRAG)
                    move = true
            }
        }

        minCon.apply {
            xOffset = BS

        }

        maxCon.apply {
            xOffset = BS*2

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
        var offset = 3 * BS + 2
        for (element in tabs.map{it -> it.tabListElement}.toList()){
            element.xOffset = offset
            offset += element.width
        }
        addCon.xOffset = offset
    }

    override fun doClientTickLocal(): Boolean {
        updatePositions()
        if (move) {
            setXYOffset(MouseHandler.mX - moveCon.xOffset - 7, MouseHandler.mY - moveCon.yOffset - 7)
        }
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(ees == EnumEventState.POST){
            RenderUtils.renderBoxWithColorBlend_s1_d0(xStart, yStart, BS*3 , 17 , RGBA.P1B1_DEF.get())
            //RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart, BS*3 + 12, 10, RGBA.BLACK_LIGHT3.get(), RGBA.P1B1_DEF.get())
        } else {

        }
        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DROP && move)
            move = false
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
