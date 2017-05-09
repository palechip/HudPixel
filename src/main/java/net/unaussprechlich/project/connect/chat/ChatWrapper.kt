package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefBackgroundContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefCustomRenderContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefWrapperContainer
import net.unaussprechlich.managedgui.lib.util.DisplayUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils


object ChatWrapper : DefWrapperContainer(){

    var isMax = false
    var move = false
    var prevX = 0
    var prevY = 0

    val stdWidth = 500
    val stdHeight = 300
    val stdChatListWidth = 130

    private var resize = false
    private var isSetup = false


    val resizeCon = DefCustomRenderContainer { xStart, yStart , _ , _, con, _ ->
        if(con.isHover || resize)   RenderUtils.iconRender_resize(xStart + con.width, yStart + con.height, RGBA.WHITE.get())
        else                        RenderUtils.iconRender_resize(xStart + con.width, yStart + con.height, RGBA.P1B1_596068.get())
    } .apply {
        width = 10
        height = 10
    }


    val controllerCon = newChatWindowControllerContainer(stdWidth).apply {
        width = stdWidth
    }

    val tabCon = newTabContainer(stdWidth - stdChatListWidth, stdHeight - controllerCon.height, { updateResizeIconPosition()}).apply {
        xOffset = stdChatListWidth
        yOffset = controllerCon.height
    }

    fun updateResizeIconPosition (){
        resizeCon.xOffset =  width - resizeCon.width - 2
        resizeCon.yOffset =  height - tabCon.chatInputField.height - resizeCon.height
    }

    val chatList = DefBackgroundContainer(RGBA.P1B1_596068.get(), stdChatListWidth, stdHeight - controllerCon.height).apply {
        yOffset = controllerCon.height
    }

    init {
        width = stdWidth
        height = stdHeight

        resizeCon.registerClickedListener { clickType, _ ->
            if(clickType == MouseHandler.ClickType.DRAG) resize = true
        }

        registerChild(tabCon)
        registerChild(controllerCon)
        registerChild(chatList)
        registerChild(resizeCon)

        minWidth  = 400
        minHeight = 200

        resizeCon.xOffset =  width - resizeCon.width - 2
        resizeCon.yOffset =  height - tabCon.chatInputField.height - resizeCon.height

        setXYOffset(20, 20)

    }

    fun resizeThatThing(width : Int, height : Int){
        val w = if(width < minWidth)   minWidth  else width
        val h = if(height < minHeight) minHeight else height

        this.width = w
        this.height = h

        controllerCon.width = w

        tabCon.width = w - stdChatListWidth
        tabCon.height = h - controllerCon.height

        chatList.height = h - controllerCon.height

        resizeCon.xOffset =  w - resizeCon.width - 2
        resizeCon.yOffset =  h - tabCon.chatInputField.height - resizeCon.height

        onResize()
    }

    override fun doClientTickLocal(): Boolean {
        if(!isSetup){
            resizeThatThing(stdWidth, stdHeight)
            isSetup = true
        }
        if (move)   ChatWrapper.setXYOffset(MouseHandler.mX - controllerCon.getMoveConXoffset() - 7, MouseHandler.mY - controllerCon.getMoveConYoffset() - 7)
        if (resize) resizeThatThing(MouseHandler.mX - xStart , MouseHandler.mY - yStart)


        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType?, isThisContainer: Boolean): Boolean {
        if (clickType == MouseHandler.ClickType.DROP && move)   move = false
        if (clickType == MouseHandler.ClickType.DROP && resize) resize = false
        return true
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent : T): Boolean {
        if(iEvent.id == EnumDefaultEvents.SCREEN_RESIZE.get()){
            if(isMax || stdWidth > DisplayUtil.scaledMcWidth - 10 || stdHeight > DisplayUtil.scaledMcHeight - 10 )
                resizeThatThing(DisplayUtil.scaledMcWidth - 10, DisplayUtil.scaledMcHeight - 10)
            else
                resizeThatThing(stdWidth, stdHeight)
            setXYOffset(5, 5)

        }
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED.get()) return false
        if (!isVisible && iEvent.id == EnumDefaultEvents.KEY_PRESSED_CODE.get()) return false
        return true
    }

}