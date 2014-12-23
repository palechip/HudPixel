package com.palechip.hudpixelmod.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;

import net.minecraftforge.fml.client.FMLClientHandler;

public class BoosterDisplay implements BoosterResponseCallback{
    private static final int REQUEST_COOLDOWN = 30000; // = 30s
    private static final int REFRESH_TIMEOUT = 120000; // 90s this is how often it refreshes when the chat gui stays open (e.g. when the person is afk)
    private static final String TITLE = EnumChatFormatting.RED + "Boosters";
    private BoosterDisplay instance;
    private long lastRequest;
    private ArrayList<String> renderingStrings;
    private ArrayList<Booster> tippedBoosters;
    private ArrayList<Booster> activeBoosters;
    private boolean isLocked;
    private boolean isInChatGui;
    private boolean hasFailed;
    
    public BoosterDisplay() {
        this.instance = this;
        this.renderingStrings = new ArrayList<String>();
        this.tippedBoosters = new ArrayList<Booster>();
        this.activeBoosters = new ArrayList<Booster>();
    }

    private void updateRenderStrings() {
        if(!isLocked) {
            renderingStrings.clear();
            renderingStrings.add(TITLE + (hasFailed ? "(Loading failed!)" : ""));
            for (Booster booster : activeBoosters) {
                // Add all active boosters. Tipped ones are white. Untipped ones are green. 
                renderingStrings.add(EnumChatFormatting.GOLD + booster.getGame().getName().replace("Survival Games", "SG").replace(" Champions", "") + ": " + (tippedBoosters.contains(booster) ? EnumChatFormatting.WHITE : EnumChatFormatting.GREEN) + booster.getOwner());
            }
        }
    }

    public void onChatMessage(String textMessage, String formattedMessage) {
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            if(textMessage.contains("You sent a") && textMessage.contains("tip of")) {
                // cut the extra stuff
                String name = textMessage.substring(0, textMessage.indexOf(" in"));
                // is it a ranked member
                if(textMessage.contains("]")) {
                    name = name.substring(name.indexOf("]") + 2);
                } else {
                    name = name.substring(name.indexOf(" to ") + 4);
                }
                // set all boosters with this name to tipped
                for (Booster booster : activeBoosters) {
                    if(booster.getOwner().equalsIgnoreCase(name)) {
                        this.tippedBoosters.add(booster);
                    }
                }

                // refresh the display strings
                this.updateRenderStrings();
            }
        }
    }
    
    public void onClientTick() {
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            Minecraft mc = FMLClientHandler.instance().getClient();
            if((mc.currentScreen instanceof GuiChat && !this.isInChatGui) || (this.isInChatGui && System.currentTimeMillis() > this.lastRequest + REFRESH_TIMEOUT)) {
                this.isInChatGui = true;
                this.requestBoosters();
            }
            if(!(mc.currentScreen instanceof GuiChat)) {
                this.isInChatGui = false;
            }
        }
    }

    private void requestBoosters() {
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            if(System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN) {
                lastRequest = System.currentTimeMillis();
                Queue.getInstance().getBoosters(instance);
            }
        }
    }

    public ArrayList<String> getRenderingStrings() {
        return renderingStrings;
    }

    @Override
    public void onBoosterResponse(ArrayList<Booster> boosters) {
        if(boosters != null) {
            this.isLocked = true;
            this.hasFailed = false;
            this.activeBoosters.clear();
            // get the active ones
            for (Booster booster : boosters) {
                // is there less than the full duration remaining
                if(booster.getRemainingTime() != booster.getTotalLength()) {
                    // it's active
                    this.activeBoosters.add(booster);
                }
            }
            this.isLocked = false;
        } else {
            this.hasFailed = true;
        }
        // make it display
        this.updateRenderStrings();
    }
}
