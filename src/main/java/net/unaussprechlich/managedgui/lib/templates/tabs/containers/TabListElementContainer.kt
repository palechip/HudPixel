/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers

import com.mojang.realmsclient.gui.ChatFormatting
import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefBackgroundContainer
import net.unaussprechlich.managedgui.lib.util.*

/**
 * TabListElementContainer Created by Alexander on 24.02.2017.
 * Description:
 */
class TabListElementContainer(internal val title: String, private val color: ColorRGBA, private val tabManager: TabManager) : DefBackgroundContainer(ConstantsMG.DEF_BACKGROUND_RGBA, FontUtil.getStringWidth(title) + 10, TabListElementContainer.ELEMENT_HEIGHT) {

    fun setOpen(open: Boolean) {
        isOpen = open
    }

    private var isOpen = false

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees == EnumEventState.PRE) return true

        if (isHover && !isOpen)
            RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart, yStart, getWidth(), ELEMENT_HEIGHT, ConstantsMG.DEF_BACKGROUND_RGBA, RGBA.BLACK_LIGHT3.get(), RGBA.BLACK_LIGHT3.get(), RGBA.BLACK_LIGHT3.get())

        if (!isOpen && !isHover)
            RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart + this.width - 8, yStart, 8, ELEMENT_HEIGHT, ConstantsMG.DEF_BACKGROUND_RGBA, RGBA.BLACK_LIGHT2.get(), RGBA.BLACK_LIGHT2.get(), RGBA.P1B1_DEF.get())

        if (isOpen)
            RenderUtils.renderBoxWithColor(xStart, yStart, width, 2, color)

        else
            RenderUtils.renderBoxWithColor(xStart, yStart + ELEMENT_HEIGHT - 2, width, 2, color)
        FontUtil.draw(ChatFormatting.GRAY.toString() + title, xStart + 5, yStart + 4)

        return true
    }

    companion object {
        val ELEMENT_HEIGHT = 17
    }
}
