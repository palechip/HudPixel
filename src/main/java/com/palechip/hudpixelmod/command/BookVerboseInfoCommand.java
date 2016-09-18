package com.palechip.hudpixelmod.command;

import com.palechip.hudpixelmod.util.HudPixelMethodHandles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BookVerboseInfoCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bookverboseinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bookverboseinfo";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        sender.addChatMessage(new ChatComponentText(BookVerboseInfo.showVerboseBookInfo ? EnumChatFormatting.RED +
                "Disabling verbose info!" : EnumChatFormatting.GREEN + "Enabling verbose info!"));
        BookVerboseInfo.showVerboseBookInfo = !BookVerboseInfo.showVerboseBookInfo;
    }

    public static class BookVerboseInfo {
        public static boolean showVerboseBookInfo;
        static {
            MinecraftForge.EVENT_BUS.register(new BookVerboseInfo());
        }
        @SubscribeEvent
        public void onGuiOpen(GuiOpenEvent event) {
            if(event.gui instanceof GuiScreenBook) {
                GuiScreenBook book = (GuiScreenBook) event.gui;
                NBTTagList tags = HudPixelMethodHandles.getBookPages(book);
                if(tags == null || !showVerboseBookInfo) return;
                for(int i = 0; i < tags.tagCount(); i++)
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(tags.getStringTagAt(i)));

            }
        }
    }
}
