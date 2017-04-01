/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.container.callback.ICallbackUpdateHeight
import net.unaussprechlich.managedgui.lib.databases.player.PlayerModel
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.EnumTime
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.helper.DateHelper
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide
import java.util.*

/**
 * DefChatMessageContainer Created by Alexander on 27.02.2017.
 * Description:
 */
class DefChatMessageContainer(private val player: PlayerModel, message: String, private val avatar_con: DefPictureContainer, width: Int) : Container() {

    private var message_con: DefTextListAutoLineBreakContainer? = null
    private val username_con: DefTextContainer

    private var callback: ICallbackUpdateHeight? = null
    private val date: DateHelper = DateHelper()

    init {
        this.username_con = DefTextContainer(player.rankName + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.dateTimeTextPassed)
        setWidth(width)
        setup(message)
        player.getPlayerHeadLoc {avatar_con.resourceLocation = it}
    }

    fun setHeightCallback(callback: ICallbackUpdateHeight) {
        this.callback = callback
    }

    val playername: String
        get() = player.name

    private fun setup(message: String) {
        avatar_con.setMargin(SPACE)
        avatar_con.width = 18
        avatar_con.height = 18
        username_con.margin = ContainerSide().BOTTOM(4).TOP(4)
        username_con.xOffset = avatar_con.widthMargin
        message_con = DefTextListAutoLineBreakContainer(message, width - avatar_con.widthMargin - SPACE - 14) { data -> update() }
        message_con!!.setXYOffset(avatar_con.widthMargin, username_con.heightMargin + 2)

        registerChild<DefTextListAutoLineBreakContainer>(message_con)
        registerChild(avatar_con)
        registerChild(username_con)

        update()
    }

    fun addMessage(s: String) {
        message_con!!.addEntry(s)
        update()
    }

    private fun update() {
        super.setHeight(SPACE * 2 + username_con.heightMargin + message_con!!.heightMargin)
        message_con!!.width = width - avatar_con.widthMargin - SPACE - 14
        if (callback != null) {
            callback!!.call(height)
        }
    }

    override fun setHeight(height: Int) {
        throw UnsupportedOperationException("[ManagedGuiLib][DefMessageContainer] setHeight() is handled automatically use setPadding() instead!")
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
        if (iEvent.id != EnumDefaultEvents.TIME.get()) return true
        if (iEvent.data == EnumTime.SEC_5) {
            username_con.text = player.rankName + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.dateTimeTextPassed
        }
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        update()
        return true
    }

    companion object {

        private val SPACE = 4

        private val styledTime: String
            get() {
                val date = Date()
                return ChatFormatting.DARK_GRAY.toString() + "" + ChatFormatting.ITALIC + " " + date.hours + ":" + date.minutes
            }
    }
}
