package com.palechip.hudpixelmod.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting

/**
 * Created by Elad on 10/28/2016.
 */
object DiscordCommand : CommandBase() {
    val array = arrayOf("e", "c", "a", "9", "D", "X", "8")
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        sender?.addChatMessage(ChatComponentText("${EnumChatFormatting.BLUE}https://discord.gg/${array.reversedArray().joinToString("")}").setChatStyle(ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/${array.reversedArray().joinToString("")}"))))
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandName(): String? {
        return "discord"
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "/discord"
    }
}