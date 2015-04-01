package com.palechip.hudpixelmod.motivation;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import com.palechip.hudpixelmod.util.ChatMessageComposer;

public class MotivationMessager {
    public static final int DELAY_IN_TICKS = 60*20; // 60s * 20 ticks/s
    public static ArrayList<String> messages;
    public static Random random;

    private int counter;

    static {
        random = new Random();
        messages = new ArrayList<String>();

        // Add all messages
        messages.add("You are the best.");
        messages.add("It's not your failture, it was a hacker!");
        messages.add("You only lost because of lag.");
        messages.add("You can do it.");
        messages.add("You are the king.");
        messages.add("\"Veni, vidi, vici\" - " + FMLClientHandler.instance().getClient().getSession().getUsername());
        messages.add("You are a good person. You are using HudPixel, you couldn't be a bad person.");
        messages.add("You are as kool as kevinkool.");
        messages.add("Everybody will remember your skill.");
        messages.add("You are so good, you must be a hacker.");
        messages.add("You don't look like you need motivition. You are so good.");
        messages.add("Without you Hypixel would be missing it's best player.");
        messages.add("Lets own some noobs.");
        messages.add("It's not about coins, it's about fun and coins.");
    }

    public MotivationMessager() {}

    public void onTick() {
        counter++;
        if(counter >= DELAY_IN_TICKS) {
            counter = 0;
            this.displayRandomMessage();
        }
    }

    private void displayRandomMessage() {
        try {
            new ChatMessageComposer(messages.get(random.nextInt(messages.size())), EnumChatFormatting.YELLOW).send();
        } catch (Exception e) {
            // ignore
        }
    }

}
