package net.unaussprechlich.project.connect.handlers

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.FMLClientHandler
import net.unaussprechlich.project.connect.gui.ChatGUI

enum class EnumHypixelChats(val command: String){
    ALL("/achat "),
    PARTY("/pchat "),
    GUILD("/gchat "),
    PRIVATE_NAME("/msg "),
    PRIVATE("/r ")
}

object HypixelChatHandler{



    fun sendHypixelMessage(text: String){
        val title = (ChatGUI.tabManager.activeTab ?: return).tabListElement.title

        if(ChatGUI.privateChatCons.containsKey(title)){
            (EnumHypixelChats.PRIVATE_NAME.command + title + " " + text).sendAsPlayer()
        }

        when((ChatGUI.tabManager.activeTab ?: return).tabListElement.title) {
            "ALL"       -> (EnumHypixelChats.ALL.command + text).sendAsPlayer()
            "PARTY"     -> (EnumHypixelChats.PARTY.command + text).sendAsPlayer()
            "GUILD"     -> (EnumHypixelChats.GUILD.command + text).sendAsPlayer()
            "PRIVATE"   -> (EnumHypixelChats.PRIVATE.command + text).sendAsPlayer()
            else -> println("wa")
        }
    }

    fun String.sendAsPlayer() {
        FMLClientHandler.instance().clientPlayerEntity.sendChatMessage(this)
    }
}


