package com.palechip.hudpixelmod.util;

import com.palechip.hudpixelmod.HudPixelMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class BetterGgHandler {
    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BetterGgHandler());
        ClientCommandHandler.instance.registerCommand(new GgCommand());
    }


    public static boolean gg = false;
    public static String chat = "";
    public static boolean first;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent e) {
        String msg = e.message.getUnformattedText();
        if (msg.startsWith("You are now in the ")) {
            if (!gg) {
                chat = msg.substring(19, 20).toLowerCase();
                System.out.println(chat);
            } else {
                e.setCanceled(true);
            }
        }
        if ((msg.startsWith("You're already in this channel")) && (gg)) {
            e.setCanceled(true);
        }
    }

    public static class GgCommand extends CommandBase {

        @Override
        public boolean canCommandSenderUseCommand(ICommandSender sender) {
            return true;
        }

        @Override
        public String getCommandName() {
            return "gg";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/gg";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) throws CommandException {
            try {
                if (HudPixelMod.isHypixelNetwork()) {
                    if (args.length > 0) {
                        String message = String.join(" ", args);
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("gg " + message);
                    } else {
                        new Thread(new GGThread()).start();
                        gg = true;
                    }
                } else {
                    String message = String.join(" ", args);
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("gg " + message);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class GGThread
            implements Runnable {
        public void run() {
            if (HudPixelMod.isHypixelNetwork()) {
                gg = true;
                if ((!Objects.equals(chat, "a")) || (!first)) {
                    first = true;
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat a");
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Minecraft.getMinecraft().thePlayer.sendChatMessage("gg");
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!Objects.equals(chat, ""))
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat " + chat);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gg = false;
            }
        }
    }

}
