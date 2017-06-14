package eladkay.hudpixel.command

import eladkay.hudpixel.util.ChatMessageComposer
import net.minecraft.client.Minecraft
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by Elad on 12/21/2016.
 */

/*
    NOTE: Most likely broken with update to MC 1.11. TODO: Fix
 */
object VerboseChatOutputCommand : HpCommandBase() {
    var enabled = false
    override fun getName() = "verbosechatoutput"

    override fun getUsage(sender: ICommandSender?) = "/verbosechatoutput"

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender == null) return
        if (enabled) {
            enabled = false
            sender.sendMessage(ChatMessageComposer("Disabled verbose chat output!").addFormatting(TextFormatting.RED).assembleMessage(true))
        } else {
            enabled = true
            sender.sendMessage(ChatMessageComposer("Enabled verbose chat output!").addFormatting(TextFormatting.GREEN).assembleMessage(true))
        }
    }

    override fun checkPermission(server: MinecraftServer?, sender: ICommandSender?): Boolean {
        return true
    }

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled) return
            // Please note that this will likely not have the desired effect. But the original method is no longer available in MC 1.11
            ChatMessageComposer(clientChatReceivedEvent.message.toString().replace("§", "&")).send(false)
            clientChatReceivedEvent.isCanceled = true
        }
    }
}

object ClickEventCommand : HpCommandBase() {
    var enabled = false
    override fun getName() = "clickevent"

    override fun getUsage(sender: ICommandSender?) = "/clickevent"

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (sender == null) return
        if (enabled) {
            enabled = false
            sender.sendMessage(ChatMessageComposer("Disabled click event chat output!").addFormatting(TextFormatting.RED).assembleMessage(true))
        } else {
            enabled = true
            sender.sendMessage(ChatMessageComposer("Enabled click event chat output!").addFormatting(TextFormatting.GREEN).assembleMessage(true))
        }
    }

    override fun checkPermission(server: MinecraftServer?, sender: ICommandSender?): Boolean {
        return true
    }

    init {
        MinecraftForge.EVENT_BUS.register(Handler)
    }

    private object Handler {
        @SubscribeEvent
        fun onChatMessage(clientChatReceivedEvent: ClientChatReceivedEvent) {
            if (!enabled || !clientChatReceivedEvent.message.toString().contains("play") || clientChatReceivedEvent.message.toString().contains("player")) return
            // Please note that this will likely not have the desired effect. But the original method is no longer available in MC 1.11
            ChatMessageComposer(clientChatReceivedEvent.message.toString().replace("§", "&")).send(false);
            clientChatReceivedEvent.isCanceled = true
        }
    }
}