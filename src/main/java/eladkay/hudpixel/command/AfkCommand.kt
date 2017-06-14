package eladkay.hudpixel.command

import eladkay.hudpixel.config.CCategory
import eladkay.hudpixel.config.ConfigPropertyString
import eladkay.hudpixel.util.ChatMessageComposer
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Elad on 12/22/2016.
 */
object AfkCommand : HpCommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (args?.size != 0) throw CommandException("AFK message not yet implemented")
        if (enabled) {
            enabled = false
            sender?.sendMessage(ChatMessageComposer("Disabled AFK mode!").addFormatting(TextFormatting.RED).assembleMessage(true));
        } else {
            enabled = true
            sender?.sendMessage(ChatMessageComposer("Enabled AFK mode!").addFormatting(TextFormatting.GREEN).assembleMessage(true));
        }
    }

    override fun getName() = "afk"
    override fun getUsage(sender: ICommandSender?) = "/afk"
    override fun checkPermission(server: MinecraftServer?, sender: ICommandSender?) = true

    var enabled = false
    @ConfigPropertyString(category = CCategory.GENERAL, id = "afkMessage", def = "I'm afk, please try again later!", comment = "Message to be displayed when people message you while /afk is active") @JvmStatic var afkMessage = "I'm afk, please try again later!"

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled) return
            if (clientChatReceivedEvent.message.unformattedText.contains("From", true)) FMLClientHandler.instance().clientPlayerEntity.sendChatMessage("/r $afkMessage")
        }
    }
}