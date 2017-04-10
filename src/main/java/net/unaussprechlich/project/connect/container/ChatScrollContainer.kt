package net.unaussprechlich.project.connect.container

import net.unaussprechlich.managedgui.lib.ConstantsMG
import net.unaussprechlich.managedgui.lib.databases.player.PlayerDatabaseMG
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefPictureContainer
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer
import net.unaussprechlich.project.connect.gui.ChatGUI

open class ChatScrollContainer : DefScrollableContainer(ConstantsMG.DEF_BACKGROUND_RGBA, ChatGUI.WIDTH, ChatGUI.HEIGHT - 17, ChatGUI.scrollSpacerRenderer){

    init {
        minWidth = 400
        minHeight= 200
    }

    fun addChatMessage(name: String, message: String, rank: Rank) {
        if (!scrollElements.isEmpty() && scrollElements[scrollElements.size - 1] is DefChatMessageContainer) {
            val conMes = scrollElements[scrollElements.size - 1] as DefChatMessageContainer
            if (conMes.playername.equals(name, ignoreCase = true)) {
                conMes.addMessage(message)
                return
            }
        }
        PlayerDatabaseMG.get(name){ player ->
            player.rank = rank
            registerScrollElement(
                    DefChatMessageContainer(
                            player,
                            message,
                            DefPictureContainer(),
                            ChatGUI.WIDTH
                    )
            )
        }

    }

}


