package eladkay.hudpixel.command

import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Elad on 12/21/2016.
 */
object VerboseChatOutputCommand : HpCommandBase() {
    var enabled = false
    override fun getCommandName() = "verbosechatoutput"

    override fun getCommandUsage(sender: ICommandSender?) = "/verbosechatoutput"

    override fun processCommand(sender: ICommandSender, args: Array<out String>?) {
        if (enabled) {
            enabled = false
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}Disabled verbose chat output!"))
        } else {
            enabled = true
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.GREEN}Enabled verbose chat output!"))
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled) return
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(clientChatReceivedEvent.message.toString().replace("§", "&")))
            clientChatReceivedEvent.isCanceled = true
        }
    }
}

object ClickEventCommand : HpCommandBase() {
    var enabled = false
    override fun getCommandName() = "clickevent"

    override fun getCommandUsage(sender: ICommandSender?) = "/clickevent"

    override fun processCommand(sender: ICommandSender, args: Array<out String>?) {
        if (enabled) {
            enabled = false
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.RED}Disabled click event chat output!"))
        } else {
            enabled = true
            sender.addChatMessage(ChatComponentText("${EnumChatFormatting.GREEN}Enabled click event chat output!"))
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled || !clientChatReceivedEvent.message.toString().contains("play") || clientChatReceivedEvent.message.toString().contains("player")) return
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(clientChatReceivedEvent.message.toString().replace("§", "&")))
            clientChatReceivedEvent.isCanceled = true
        }
    }
}