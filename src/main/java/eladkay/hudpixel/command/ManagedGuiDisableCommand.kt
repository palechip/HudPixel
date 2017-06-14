package eladkay.hudpixel.command

import eladkay.hudpixel.util.ChatMessageComposer
import eladkay.hudpixel.util.plus
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextFormatting
import net.unaussprechlich.managedgui.lib.ManagedGui

/**
 * Created by Elad on 3/2/2017.
 */
object ManagedGuiDisableCommand : HpCommandBase() {

    override fun checkPermission(server: MinecraftServer?, sender: ICommandSender?): Boolean {
        return true
    }

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (ManagedGui.isIsDisabled) {
            ChatMessageComposer("Disabled managed GUI").addFormatting(TextFormatting.GREEN).send()
            ManagedGui.isIsDisabled = false
        } else {
            ChatMessageComposer("Disabled managed GUI").addFormatting(TextFormatting.RED).send()
            ManagedGui.isIsDisabled = true
        }
    }

    override fun getName(): String {
        return "mgd"
    }

    override fun getUsage(sender: ICommandSender?): String {
        return "/mgd"
    }
}