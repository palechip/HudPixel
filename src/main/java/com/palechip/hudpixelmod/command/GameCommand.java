package com.palechip.hudpixelmod.command;

import com.palechip.hudpixelmod.modulargui.modules.PlayGameModularGuiProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GameCommand extends CommandBase {


    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "game";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/game <Game ID>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 1) {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        } else {
            PlayGameModularGuiProvider.content = args[0];
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Game set!"));
        }
    }
}
