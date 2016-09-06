package com.palechip.hudpixelmod.command;

import com.palechip.hudpixelmod.util.ChatMessageComposer;
import com.palechip.hudpixelmod.util.ScoreboardReader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import static com.palechip.hudpixelmod.GameDetector.stripColor;

public class ScoreboardCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "sb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sb";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ScoreboardReader.resetCache();
        String title = ScoreboardReader.getScoreboardTitle();
        title = stripColor(title).toLowerCase();
        System.out.println(title);
        new ChatMessageComposer("\"" + title + "\"").send();
    }




}
