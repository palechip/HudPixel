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
import net.unaussprechlich.managedgui.lib.util.*


class DefCheckboxContainer (val text : String, var textColor : ColorRGBA = RGBA.NULL.get()) : Container(){

    private val checkBoxRenderer = object : ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true
            val w = 9
            val h = 9
            RenderUtils.drawBorderInlineShadowBox(xStart, yStart, w, h, RGBA.P1B1_596068.get(), RGBA.P1B1_DEF.get())
            if(isChecked)
                RenderUtils.renderBoxWithColor(xStart + 2, yStart +2 , w - 4, h - 4, RGBA.P1B1_596068.get())
            return true
        }

    }

    private val checkBox = DefCustomRenderContainer(checkBoxRenderer)

    var isChecked = false

    init {
        registerChild(checkBox)
        checkBox.apply {
            width = 9
            height = 9
        }
        checkBox.registerClickedListener( { clickType, _ ->
            if(clickType == MouseHandler.ClickType.SINGLE){
                isChecked = !isChecked
                System.out.println("" + clickType.name + "  " + isChecked)
            } })
    }



    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState?): Boolean {
        if(ees == EnumEventState.POST) return true

        if(textColor == RGBA.NULL.get())
            FontUtil.draw(text, xStart + 15, yStart )
        else
            FontUtil.drawWithColor(text, xStart + 15, yStart  , textColor)

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

    override fun <T : Event<*>?> doEventBusLocal(iEvent: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent?): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}