package net.unaussprechlich.project.connect.container

import com.mojang.realmsclient.gui.ChatFormatting
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefButtonContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

class ChatTabContainer(tabListElement: TabListElementContainer, container: ChatScrollContainer, tabManager: TabManager) : TabContainer(tabListElement, container, tabManager) {

    //600 commits <3

    var unread = 0
    var isUnreadNotify = false
    var unreadNotify = DefButtonContainer(FontUtil.getStringWidth(unread.toString()) + 5, 9,
            {_, _ -> },
            { xStart, yStart, width, height ->
                RenderUtils.renderBoxWithColor(xStart, yStart - 1, width, height, RGBA.RED_MC.get())
                FontUtil.draw("" + ChatFormatting.BOLD + unread.toString(), xStart + 2, yStart - 1 )
            },{ xStart, yStart, width, height ->
                RenderUtils.renderBoxWithColor(xStart, yStart - 1, width, height, RGBA.RED_MC.get())
                FontUtil.draw("" + ChatFormatting.BOLD + unread.toString(), xStart + 2, yStart -1 )
            }
    )

    val textField = ChatTextFieldContainer("", container.width, {})

    init {
        setClosed()
        registerChild(textField)
    }


    fun addChatMessage(name: String, message: String, rank: Rank) {
        if(tabManager.activeTab != this){
            unread++
            unreadNotify.width = FontUtil.getStringWidth(unread.toString()) + 5
        }
        (container as ChatScrollContainer).addChatMessage(name, message, rank)
        if(unread > 0 && !isUnreadNotify){
            tabListElement.registerButton(unreadNotify)
            isUnreadNotify = true
        }
    }

    override fun doClientTickLocal(): Boolean {
        textField.apply {
            yOffset =  container.height + tabListElement.height
            width = container.width + 2
        }

        return super.doClientTickLocal()
    }

    override fun setClosed() {
        super.setClosed()
        textField.isVisible = false
    }

    override fun setOpen() {
        super.setOpen()
        unread = 0
        tabListElement.unregisterButton(unreadNotify)
        isUnreadNotify = false
        textField.isVisible = true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        RenderUtils.renderBoxWithColorBlend_s1_d0(xStart + container.width, yStart + tabListElement.height, 2, container.height, RGBA.P1B1_DEF.get())
        return super.doRenderTickLocal(xStart, yStart, width, height, ees)
    }

}


