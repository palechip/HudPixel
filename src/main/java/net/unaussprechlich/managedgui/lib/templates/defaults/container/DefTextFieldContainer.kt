/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.unaussprechlich.managedgui.lib.ConstantsMG
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
import java.util.*

typealias IKeyListener = (Char) -> Unit

class DefTextFieldContainer(text: String, width: Int) : DefTextAutoLineBreakContainer(text, width) {

    var hasFocus = false
    var cursorBlink = false
    var cursorPos = 0
    var cursorX = 0
    var cursorY = 0



    private val keyListener = ArrayList<IKeyListener>()

    fun registerKeyListener(function : IKeyListener){
        keyListener.add(function)
    }

    fun unregisterKeyListener(function : IKeyListener){
        keyListener.remove(function)
    }

    init {
        registerClickedListener { clickType, container ->
            if(clickType == MouseHandler.ClickType.SINGLE) hasFocus = !hasFocus
        }
        cursorPos = text.length
    }


    override fun onUpdate(){

    }


    fun updateCursor(){
        println("UPDATE THIS SHIT NOW U SON OF A BICTHNIOJCVE KJNÖEQF")
        var index = 0
        var row = 0
        for(s in renderList){
            println((index + s.length).toString() + " | " + cursorPos)
            if((index + s.length) >= cursorPos){
                cursorX = FontUtil.getStringWidth(s.substring(0, cursorPos - index) )
                cursorY = row * ConstantsMG.TEXT_Y_OFFSET
            } else {
                index += s.length
                row++
            }
        }
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        super.doRenderTickLocal(xStart + 5 , yStart + 5, width, height, ees)
        if(ees == EnumEventState.POST) return true

        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart, yStart, width + 50, height + 12, RGBA.P1B1_DEF.get())
        RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + 3, yStart + 3, height + 6, width + 10, RGBA.BLACK_LIGHT.get(), RGBA.P1B1_DEF.get(), 2)

        if(cursorBlink)
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 5 + cursorX, yStart + 5 + cursorY, 1, 9, RGBA.P1B1_596068.get())


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
                    if(text.isNotEmpty())
                        if(cursorPos == text.length){
                            text = text.substring(0, text.length -1)
                            cursorPos--
                        } else{
                            text = text.substring(0, cursorPos - 1) + text.substring(cursorPos, text.length)
                            if(cursorPos > 0) cursorPos--
                        }
                }
                205 -> {
                    if(cursorPos + 1 <= text.length) cursorPos++
                }
                203 -> {
                    if(cursorPos > 0) cursorPos--
                }
                //else -> println("data: ${iEvent.data.toInt()}")
            }
            updateCursor()
            keyListener.forEach { iEvent.data.toChar() }
            return true
        }

        if (iEvent.id != EnumDefaultEvents.KEY_PRESSED.get()) return true
        val c = (iEvent as KeyPressedEvent).data.toCharArray()[0]

        keyListener.forEach {c}

        if(cursorPos == text.length){
            text += c
            cursorPos++
        } else {
            text = text.substring(0, cursorPos) + c + text.substring(cursorPos, text.length)
            cursorPos++
        }

        return true
    }


}