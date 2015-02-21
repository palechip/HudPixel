package com.palechip.hudpixelmod.detectors;

import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

/**
 * As long as 1.8 needs my tweaked Forge this is need to help players who still end up in Limbo.
 */
public class LimboStuckDetector {
    private static final long TICKS_BETWEEN_MESSAGES = 100;
    private static final String RELEASE_LINK = "https://github.com/palechip/HudPixel/releases/latest";
    private static final String MIRROR_LINK = "https://www.dropbox.com/s/vua58k5hsy2xw6s/forge-1.8-11.14.0.1289-1.8-bungeeServerTweak-installer.jar?dl=1";
    private static final String FORUM_LINK = "http://hypixel.net/threads/client-mod-hudpixel-reloaded-overlay-with-extra-information.204572/";
    private boolean bungeeProblemIdentified = false;
    private long waitTicks = 50;
    private long ticksUntilMessage = TICKS_BETWEEN_MESSAGES;
    private int messageNo = 0;

    public LimboStuckDetector() {}
    
    public void onChatMessage(String textMessage, String formattedMessage) {
        // catch the triggered exception
        if(textMessage.contains("Malformed data feed! [mc.hypixel.net, FML, ,")) {
            this.bungeeProblemIdentified = true;
        }
        // second trigger
        if(textMessage.contains("net.md_5.bungee.ServerConnector:95")) {
            this.bungeeProblemIdentified = true;
        }
    }

    public void onClientTick() {
        if(this.bungeeProblemIdentified) {
            // log the detection
            if(this.waitTicks == 50) {
                HudPixelMod.instance().logError("FOUND INCOMPATIBLE FORGE VERSION! Please download the version provided at the release. This one won't work.");
            }
            
            if(this.waitTicks > 0) {
                // count down the wait time 
                this.waitTicks--;
            } else {
                // run the message programm
                this.runMessageProgram();
            }
        }
    }
    
    protected void runMessageProgram() {
        if(this.messageNo <= 5) {
            this.ticksUntilMessage--;
            if(this.ticksUntilMessage <= 0) {
                this.ticksUntilMessage = TICKS_BETWEEN_MESSAGES;
                switch (this.messageNo++) {
                case 0:
                    new ChatMessageComposer("1111", EnumChatFormatting.RED).addFormatting(EnumChatFormatting.OBFUSCATED).appendMessage(new ChatMessageComposer(" Detected you being stuck in Limbo! Pay attention for the solution!", EnumChatFormatting.RED)).send();
                    break;
                case 1:
                    new ChatMessageComposer("The official version of Minecraft Forge is currently incompatible with modded servers like Hypixel.", EnumChatFormatting.LIGHT_PURPLE).send();
                    break;
                case 2: 
                    new ChatMessageComposer("But it is possible to modify Forge to solve this problem!", EnumChatFormatting.LIGHT_PURPLE).send();
                    break;
                case 3:
                    new ChatMessageComposer("You have to download the modified version on the ", EnumChatFormatting.LIGHT_PURPLE).appendMessage(new ChatMessageComposer("HudPixel Download Page.", EnumChatFormatting.GOLD).makeClickable(Action.OPEN_URL, RELEASE_LINK, new ChatMessageComposer("Open it in your browser!", EnumChatFormatting.DARK_AQUA))).send();
                    break;
                case 4:
                    new ChatMessageComposer("Alternatively here is a ", EnumChatFormatting.LIGHT_PURPLE).appendMessage(new ChatMessageComposer("direct link.", EnumChatFormatting.GOLD).makeClickable(Action.OPEN_URL, MIRROR_LINK, new ChatMessageComposer("Open it in your browser!", EnumChatFormatting.DARK_AQUA))).send();
                    break;
                case 5:
                    new ChatMessageComposer("If this can't help you, ask for help at ", EnumChatFormatting.LIGHT_PURPLE).appendMessage(new ChatMessageComposer("this forum thread.", EnumChatFormatting.GOLD).makeClickable(Action.OPEN_URL, FORUM_LINK, new ChatMessageComposer("Open it in your browser!", EnumChatFormatting.DARK_AQUA))).send();
                    break;
                }
            }
        }
    }
}
