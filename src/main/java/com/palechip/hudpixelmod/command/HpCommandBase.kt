package com.palechip.hudpixelmod.command

import net.minecraft.command.CommandBase
import net.minecraftforge.client.ClientCommandHandler

/**
 * Created by Elad on 3/13/2017.
 */
abstract class HpCommandBase : CommandBase() {
    init {
        ClientCommandHandler.instance.registerCommand(this)
    }
}