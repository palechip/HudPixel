package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefBackgroundContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefWrapperContainer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA


object ChatWrapper : DefWrapperContainer(){

    var isMax = false
    var move = false
    var prevX = 0
    var prevY = 0

    val stdWidth = 500
    val stdHeight = 300
    val stdChatListWidth = 130

    private val controllerCon = newChatWindowControllerContainer(stdWidth).apply {
        width = stdWidth
    }

    private val tabCon = newTabContainer(stdWidth - stdChatListWidth, stdHeight - controllerCon.height).apply {
        xOffset = stdChatListWidth
        yOffset = controllerCon.height
    }

    private val chatList = DefBackgroundContainer(RGBA.P1B1_596068.get(), stdChatListWidth, stdHeight - controllerCon.height).apply {
        yOffset = controllerCon.height
    }

    init {
        width = stdWidth
        height = stdHeight

        registerChild(tabCon)
        registerChild(controllerCon)
        registerChild(chatList)

        minWidth  = 400
        minHeight = 200

        setXYOffset(20, 20)
    }

    override fun doClientTickLocal(): Boolean {
        if (move) ChatWrapper.setXYOffset(MouseHandler.mX - controllerCon.getMoveConXoffset() - 7, MouseHandler.mY - controllerCon.getMoveConYoffset() - 7)
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType?, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DROP && move) move = false
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent : T): Boolean {
        if(iEvent.id == EnumDefaultEvents.SCREEN_RESIZE.get() && isVisible && isMax ){
            setXYOffset(5, 5)
            width = DisplayUtil.scaledMcWidth
            height = DisplayUtil.scaledMcHeight
        }
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED.get()) return false
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()) return false
        return true
    }

}