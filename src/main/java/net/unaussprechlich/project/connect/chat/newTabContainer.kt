package net.unaussprechlich.project.connect.chat

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils
import net.unaussprechlich.project.connect.container.ChatTextFieldContainer


/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */


/**
 * TabContainer Created by Alexander on 24.02.2017.
 * Description:
 */
class newTabContainer(width: Int, height: Int, val sizeCallback: () -> Unit) : Container() {

    val chatConSpacerRenderer = object : IScrollSpacerRenderer {
        override fun render(xStart: Int, yStart: Int, width: Int) {
            RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + 25, yStart, width - 42, 1, RGBA.P1B1_596068.get())
        }
        override val spacerHeight: Int
            get() = 1
    }

    val scrollCon = newChatScrollListContainer(chatConSpacerRenderer)
    val chatInputField = ChatTextFieldContainer("", width, { _ ->
        update()
        sizeCallback.invoke()
    })

    init {
        this.width = width
        this.height = height

        registerChild(scrollCon)
        registerChild(chatInputField)

        chatInputField.width = width
        chatInputField.yOffset = height - chatInputField.height
        scrollCon.width = width
        scrollCon.height = height - chatInputField.height

    }

    fun update(){
        chatInputField.width = width
        chatInputField.yOffset = height - chatInputField.height
        scrollCon.width = width
        scrollCon.height = height - chatInputField.height
    }

    /*

    fun loadChat(chat : Chat){

    }

    fun unloadChat(): Chat{

    }

    */

    override fun doClientTickLocal(): Boolean {
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
        update()
        return true
    }
}
