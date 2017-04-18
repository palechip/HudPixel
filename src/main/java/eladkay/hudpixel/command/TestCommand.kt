package eladkay.hudpixel.command

import net.minecraft.command.ICommandSender

/**
 * Created by Elad on 3/13/2017.
 */
object TestCommand : HpCommandBase() {
    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {

    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }

    override fun getCommandName(): String {
        return "hptest"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/hptest"
    }
}