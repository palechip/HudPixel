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
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.ICustomRenderer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
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

    private val minIconRenderer = object: ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s - 4, width - s2*2 - 2, 1, RGBA.P1B1_596068.get())

            return true
        }
    }

    private val maxIconRenderer = object: ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, RGBA.P1B1_596068.get())

            if(isMax){
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height / 2 , 4, 1, RGBA.P1B1_596068.get())
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 5, 1, 3, RGBA.P1B1_596068.get())

                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 5, yStart + s2 + 1, 4, 1, RGBA.P1B1_596068.get())
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + s2 + 2, 1, 3, RGBA.P1B1_596068.get())
            } else {
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 2, 4, 1, RGBA.P1B1_596068.get())
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 5, 1, 3, RGBA.P1B1_596068.get())

                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 5, yStart + s2 + 1, 4, 1, RGBA.P1B1_596068.get())
                RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + s2 + 2, 1, 3, RGBA.P1B1_596068.get())
            }


            return true
        }
    }

    private val moveIconRenderer = object: ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val s = 2
            val s2 = s*2

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + s +1 , yStart + s +1, width - s2 - 2, height - s2 -2, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s, width - s2, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s -1, yStart + s + 1, 1, height - 2 - s2, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s, yStart + height - s - 1, width - s2, 1, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 2, 3, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + height - s2 - 4, 1, 2, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + s2 + 1, 3, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + s2 + 2, 1, 2, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 4, yStart + height - s2 - 2, 3, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + width - s2 - 2, yStart + height - s2 - 4, 1, 2, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + s2 + 1, 3, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + s2 + 1, yStart + s2 + 2, 1, 2, RGBA.P1B1_596068.get())

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart +  width / 2, yStart + height /2 , 1, 1, RGBA.P1B1_596068.get())

            return true
        }
    }

    private val maxCon = DefCustomRenderContainer(maxIconRenderer).apply {
        xOffset = BS * 2 + 2
        width = BS
        height = BS
    }

    private val moveCon = DefCustomRenderContainer(moveIconRenderer).apply {
        width = BS
        height = BS
        xOffset = 2
    }

    private val minCon = DefCustomRenderContainer(minIconRenderer).apply {
        xOffset = BS + 2
        width = BS
        height = BS
    }

    private var prevX = 0
    private var prevY = 0
    private var isMax = false

    private val addCon =  DefPictureContainer(17, 17, ImageLoader.chatTabAddLocation())
    internal var activeTab: TabContainer? = null
    private var move = false

    init {
        registerChild(moveCon)
        registerChild(maxCon)
        registerChild(minCon)
        registerChild(addCon)

        moveCon.registerClickedListener { clickType, container ->
            if (clickType == MouseHandler.ClickType.DRAG)
                move = true
        }

        maxCon.registerClickedListener({ clickType, container ->
            if(clickType == MouseHandler.ClickType.SINGLE){
                if(isMax){
                    isMax = false
                } else {
                    isMax = true
                    prevX = xOffset
                    prevY = yOffset
                    setXYOffset(0, 0)
                    width = DisplayUtil.scaledMcWidth
                    height = DisplayUtil.scaledMcHeight
                }
            }
        })


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
        var offset = 3 * BS + 6
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
            RenderUtils.renderBoxWithColorBlend_s1_d0(xStart, yStart, BS*3 + 4 , 17 , RGBA.P1B1_DEF.get())
            RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart, BS*3 + 4, 10, RGBA.BLACK_LIGHT2.get(), RGBA.P1B1_DEF.get())
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
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED.get()) return false
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()) return false
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
