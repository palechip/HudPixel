/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedEvent
import net.unaussprechlich.managedgui.lib.event.events.TimeEvent
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

open class DefPasswordFieldContainer(val hint : String = "", widthBox : Int = 100, val maxLenght: Int = 30) : Container() {

    var text = ""
    var realText = ""
    var hasFocus = false
    var cursorBlink = false
    var cursorPos = 0
    var cursorX = 0

    init {
        width = widthBox
        height = 17
        minHeight = 17
        registerClickedListener { clickType, container ->
            if(clickType == MouseHandler.ClickType.SINGLE) hasFocus = !hasFocus
        }
        cursorPos = text.length
    }

    fun updateCursor(){
        if(realText == "")
            text = ""
        else {
            var newText = ""
            repeat(realText.length - 1, {newText += "*"})
            newText += realText.last()
            text = newText
        }

        cursorX = FontUtil.getStringWidth(text.substring(0, cursorPos))
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(ees == EnumEventState.POST) return true

        RenderUtils.drawBorderInlineShadowBox(xStart, yStart, width, height, RGBA.P1B1_596068.get(), RGBA.P1B1_DEF.get()  )

        if(realText == "" && hint != "")
            FontUtil.draw("" + TextFormatting.GRAY + TextFormatting.ITALIC + hint, xStart+5, yStart+4)
        else
            FontUtil.draw(text, xStart + 5 , yStart + 4)

        if(cursorBlink)
            RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 5 + cursorX, yStart + 4, 1, 9, RGBA.P1B1_596068.get())

        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if(!isVisible) return false

        if (iEvent.id == EnumDefaultEvents.TIME.get())
            if((iEvent as TimeEvent).data == EnumTime.TICK_15){
                cursorBlink = !cursorBlink

                return true
            }

        if(iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()){
            when((iEvent as KeyPressedCodeEvent).data){
                14 -> {
                    if(realText.isNotEmpty())
                        if(cursorPos == realText.length){
                            realText = realText.substring(0, realText.length -1)
                            cursorPos--
                        } else{
                            realText = realText.substring(0, cursorPos - 1) + realText.substring(cursorPos, realText.length)
                            if(cursorPos > 0) cursorPos--
                        }
                }
                205 -> {
                    if(cursorPos + 1 <= realText.length) cursorPos++
                }
                203 -> {
                    if(cursorPos > 0) cursorPos--
                }
            //else -> println("data: ${iEvent.data.toInt()}")
            }
            updateCursor()
            return true
        }

        if (iEvent.id != EnumDefaultEvents.KEY_PRESSED.get()) return true
        val c = (iEvent as KeyPressedEvent).data.toCharArray()[0]

        if(realText.length + 1 > maxLenght) return true

        if(cursorPos == realText.length){
            realText += c
            cursorPos++
        } else {
            realText = realText.substring(0, cursorPos) + c + realText.substring(cursorPos, realText.length)
            cursorPos++
        }

        return true
    }

    override fun doClientTickLocal(): Boolean {
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

    override fun doOpenGUILocal(e: GuiOpenEvent?): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }



}


