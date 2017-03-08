package net.unaussprechlich.project.connect.container

import net.minecraft.client.Minecraft
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.events.KeyPressedCodeEvent
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefTextFieldContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.ICustomRenderer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.gui.ChatGUI

class ChatTextFieldContainer(text: String, width: Int) : DefTextFieldContainer(text, width){


    private val sendIconRenderer = object: ICustomRenderer{
        override fun onRender(xStart: Int, yStart: Int, width: Int, height: Int, con: Container, ees: EnumEventState): Boolean {
            if(ees == EnumEventState.POST) return true

            val offY = (height - 7) / 2

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart, yStart, height , width, RGBA.BLACK_LIGHT.get(), RGBA.P1B1_DEF.get(), 2)

            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY     , buttonWidth - 9, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY + 3 , buttonWidth - 9, 1, RGBA.P1B1_596068.get())
            RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + 4, yStart + offY + 6 , buttonWidth - 9, 1, RGBA.P1B1_596068.get())

            return true
        }
    }

    val sendButton = DefCustomRenderContainer(sendIconRenderer)

    val buttonWidth = 20
    var localWidth = width

    init {
        registerChild(sendButton)
        sendButton.registerClickedListener { clickType, container ->
            if(clickType == MouseHandler.ClickType.SINGLE) send()
        }
    }

    fun String.sendAsPlayer() {
        Minecraft.getMinecraft().thePlayer?.sendChatMessage(this)
    }
    fun min(a: Int, b: Int) = if(a < b) a else b
    fun send(title: String = "") {
        if(title != "") {
            val title = title.substring(0..min(title.length - 1, 100))
            "/$title $text".sendAsPlayer()
            println("wa1")
            return
        }
        //val text = text.substring(0..min(title.length - 1, 100))
        if(text.isEmpty()) {
            return
        }
        when((ChatGUI.tabManager.activeTab ?: return).tabListElement.title) {
            "ALL" -> "/achat $text".sendAsPlayer()
            "PARTY" -> "/pchat $text".sendAsPlayer()
            "GUILD" -> "/gchat $text".sendAsPlayer()
            "PRIVATE" -> "/r $text".sendAsPlayer()
            else -> println("wa")
        }
    }



    fun setWidthLocal(width: Int){
        super.setWidth(width - buttonWidth - 31)
        localWidth = width
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        super.doRenderTickLocal(xStart, yStart, width, height, ees)
        if(ees == EnumEventState.POST) return true

        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + localWidth - buttonWidth , yStart, buttonWidth , height+ 12 , RGBA.P1B1_DEF.get())

        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + localWidth - buttonWidth - 2, yStart + 2 , buttonWidth , 1, RGBA.P1B1_596068.get())
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + localWidth - 3 , yStart + 3, 1, height + 6, RGBA.P1B1_596068.get())
        RenderUtils.renderBoxWithColorBlend_s1_d1(xStart + localWidth - buttonWidth - 2, yStart + height + 9,buttonWidth , 1, RGBA.P1B1_596068.get())

        sendButton.apply {
            xOffset = localWidth - buttonWidth - 2
            yOffset = 3
            sendButton.height = height + 6
            sendButton.width = buttonWidth - 1
        }

        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        if(iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()){
            when((iEvent as KeyPressedCodeEvent).data){
                28 -> send()
                else -> println("data: ${iEvent.data.toInt()}")
            }
            updateCursor()
        }
        return super.doEventBusLocal(iEvent)
    }

}


