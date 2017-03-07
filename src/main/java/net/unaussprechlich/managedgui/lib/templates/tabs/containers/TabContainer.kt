/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers

import com.mojang.realmsclient.gui.ChatFormatting
import com.palechip.hudpixelmod.util.KeyTracker
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefTextContainer
import net.unaussprechlich.managedgui.lib.util.EnumEventState

/**
 * TabContainer Created by Alexander on 24.02.2017.
 * Description:
 */
class TabContainer(val tabListElement: TabListElementContainer, val container: Container, private val tabManager: TabManager) : Container() {

    private val chatField = DefTextContainer("" + ChatFormatting.DARK_RED + "Hello Elad!").apply { setXYOffset(0, 190) }

    init {
        registerChild(this.container)
        registerChild(this.tabListElement)
        //registerChild(chatField)
        container.yOffset = TabListElementContainer.ELEMENT_HEIGHT
        tabListElement.registerClickedListener { clickType, container -> if (clickType == MouseHandler.ClickType.SINGLE) setActive() }
        setClosed()
        KeyTracker.register {
            when(it) {
                8.toChar() -> {
                    if(chatField.text.isNotEmpty())
                        chatField.text = chatField.text.substring(0 until chatField.text.length - 1)
                }
                13.toChar() -> chatField.text = ""
                else -> {
                    chatField += it.also { println(it.toInt()) }
                }
            }
        }
    }

    fun setActive() {
        tabManager.setTabActive(this)
    }

    fun setClosed() {
        tabListElement.setOpen(false)
        container.isVisible = false
    }

    fun setOpen() {
        tabListElement.setOpen(true)
        container.isVisible = true
    }

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
        return true
    }
}
