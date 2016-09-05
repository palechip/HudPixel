package com.palechip.hudpixelmod.command;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.util.ChatMessageComposer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class GameDetectorCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "gd";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gd";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        new ChatMessageComposer(GameDetector.getCurrentGameType().toString()).send();
    }
}
