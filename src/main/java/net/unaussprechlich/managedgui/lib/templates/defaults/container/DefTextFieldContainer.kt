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
import net.unaussprechlich.managedgui.lib.ConstantsMG
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

class DefTextFieldContainer(text: String, width: Int, var hint : String = "", val sizeCallback : (height : Int) -> Unit) : Container() {

    override fun doResizeLocal(width: Int, height: Int): Boolean { return true }

    val textCon =  DefTextAutoLineBreakContainer(text, width - 20, { h ->
        update()
    }).apply {
        yOffset = 5
        xOffset = 5
    }

    var hasFocus = false
    var cursorBlink = false
    var cursorPos = 0
    var cursorX = 0
    var cursorY = 0

    init {
        registerChild(textCon)

        this.width = width

        textCon.registerClickedListener { clickType, _ ->
            if(clickType == MouseHandler.ClickType.SINGLE) hasFocus = !hasFocus
        }

        cursorPos = text.length
        backgroundRGBA = RGBA.P1B1_DEF.get()
    }

    fun update(){
        height = textCon.height + 10
        textCon.width = width - 27
        sizeCallback.invoke(height)
    }

    fun updateCursor(){
        var index = 0
        var row = 0
        for(s in textCon.renderList){
            if((index + s.length) >= cursorPos - row){
                cursorX = FontUtil.getStringWidth(s.substring(0, cursorPos - index - row) )
                cursorY = row * ConstantsMG.TEXT_Y_OFFSET
            } else {
                index += s.length
                row++
            }
        }
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if(ees == EnumEventState.PRE) return true

        RenderUtils.drawBorderInlineShadowBox(xStart + 2 , yStart + 2, width - 4, height - 4, RGBA.P1B1_596068.get(), RGBA.P1B1_DEF.get())

        if(textCon.text == "" && hint != "") FontUtil.draw("" + TextFormatting.GRAY + TextFormatting.ITALIC + hint, xStart+5, yStart+5)
        if(cursorBlink)                      RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 5 + cursorX, yStart + 5 + cursorY, 1, 9, RGBA.P1B1_596068.get())

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
                    if(textCon.text.isNotEmpty())
                        if(cursorPos == textCon.text.length){
                            textCon.text = textCon.text.substring(0, textCon.text.length -1)
                            cursorPos--
                        } else{
                            textCon.text = textCon.text.substring(0, cursorPos - 1) + textCon.text.substring(cursorPos, textCon.text.length)
                            if(cursorPos > 0) cursorPos--
                        }
                }
                205 -> {
                    if(cursorPos + 1 <= textCon.text.length) cursorPos++
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


        if(cursorPos == textCon.text.length){
            textCon.text += c
            cursorPos++
        } else {
            textCon.text = textCon.text.substring(0, cursorPos) + c + textCon.text.substring(cursorPos, textCon.text.length)
            cursorPos++
        }

        return true
    }

    override fun doClientTickLocal(): Boolean { return true }
    override fun doChatMessageLocal(e: ClientChatReceivedEvent?): Boolean { return true }
    override fun doClickLocal(clickType: MouseHandler.ClickType?, isThisContainer: Boolean): Boolean { return true }
    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean { return true }
    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean { return true }
    override fun doOpenGUILocal(e: GuiOpenEvent?): Boolean { return true }


}