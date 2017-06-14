package eladkay.hudpixel.util;


import eladkay.hudpixel.HudPixelMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
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
        String msg = e.getMessage().getUnformattedText();
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
        public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
            return true;
        }

        @Override
        public String getName() {
            return "gg";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "/gg";
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            try {
                if (HudPixelMod.Companion.isHypixelNetwork()) {
                    if (args.length > 0) {
                        String message = String.join(" ", args);
                        FMLClientHandler.instance().getClientPlayerEntity().sendChatMessage("gg " + message);
                    } else {
                        new Thread(new GGThread()).start();
                        gg = true;
                    }
                } else {
                    String message = String.join(" ", args);
                    FMLClientHandler.instance().getClientPlayerEntity().sendChatMessage("gg " + message);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static class GGThread
            implements Runnable {
        public void run() {
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                gg = true;
                if ((!Objects.equals(chat, "a")) || (!first)) {
                    first = true;
                    FMLClientHandler.instance().getClientPlayerEntity().sendChatMessage("/chat a");
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                FMLClientHandler.instance().getClientPlayerEntity().sendChatMessage("gg");
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!Objects.equals(chat, ""))
                    FMLClientHandler.instance().getClientPlayerEntity().sendChatMessage("/chat " + chat);
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
