package com.palechip.hudpixelmod.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

/**
 * Created by Elad on 10/28/2016.
 */

object DiscordCommand : CommandBase() {
    override fun getCommandName(): String {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

/*
object DiscordCommand : CommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val array = arrayOf("e", "c", "a", "9", "D", "X", "8")
    /*override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        sender?.addChatMessage(ChatComponentText("${ChatFormatting.RED}Please please please read #announcements before asking any questions about anything related to the server itself"))
        sender?.addChatMessage(ChatComponentText("${ChatFormatting.BLUE}https://discord.gg/${array.reversedArray().joinToString("")}").setChatStyle(ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/${array.reversedArray().joinToString("")}"))))
    }*/

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandName(): String? {
        return "discord"
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "/discord"
    }
}*/


