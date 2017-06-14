package eladkay.hudpixel.command

import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

/**
 * Created by Elad on 3/13/2017.
 */
object TestCommand : HpCommandBase() {
    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {

    }

    override fun checkPermission(server: MinecraftServer?, sender: ICommandSender?): Boolean {
        return true
    }

    override fun getName(): String {
        return "hptest"
    }

    override fun getUsage(sender: ICommandSender?): String {
        return "/hptest"
    }
}