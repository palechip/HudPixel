package com.palechip.hudpixelmod.command

import com.palechip.hudpixelmod.config.CCategory
import com.palechip.hudpixelmod.config.ConfigPropertyString
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Elad on 12/22/2016.
 */
object AfkCommand : HpCommandBase() {
    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        if (args.size != 0) throw CommandException("AFK message not yet implemented")
        if (enabled) {
            enabled = false
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}Disabled AFK mode!"))
        } else {
            enabled = true
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.GREEN}Enabled AFK mode!"))
        }
    }

    override fun getCommandName() = "afk"
    override fun getCommandUsage(sender: ICommandSender?) = "/afk"
    override fun canCommandSenderUseCommand(sender: ICommandSender?) = true
    var enabled = false
    @ConfigPropertyString(category = CCategory.GENERAL, id = "afkMessage", def = "I'm afk, please try again later!", comment = "Message to be displayed when people message you while /afk is active") @JvmStatic var afkMessage = "I'm afk, please try again later!"

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled) return
            if (clientChatReceivedEvent.message.unformattedText.contains("From", true)) Minecraft.getMinecraft().thePlayer.sendChatMessage("/r $afkMessage")
        }
    }
}