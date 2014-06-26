package com.palechip.hudpixelmod.components;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;

public class PaintballKillstreakTimer implements IComponent {
    private String renderedString;

    private String listenedKillstreak;
    private boolean isActive;
    private long startTime;
    private long duration;


    public PaintballKillstreakTimer(String killstreak) {
        this.listenedKillstreak = killstreak;
        this.renderedString = "";
    }

    @Override
    public void setupNewGame() {
        this.renderedString = "";
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        this.isActive = false;
    }

    @Override
    public void onTickUpdate() {
        if(this.isActive) {
            // only if we already measured the lenght
            if( duration > 0) {
                long remainingTime = this.duration - (System.currentTimeMillis() - this.startTime);
                this.renderedString = this.getColorForTime(remainingTime / 1000) + remainingTime / 1000 + "s";
            } else {
                long timePast = System.currentTimeMillis() - this.startTime;
                this.renderedString = EnumChatFormatting.YELLOW + "" + timePast / 1000 + "s (m)";
            }
        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // test for starting
        if(textMessage.contains(FMLClientHandler.instance().getClient().getSession().getUsername() + " purchased " + this.listenedKillstreak)) {
            this.startTime = System.currentTimeMillis();
            this.isActive = true;
        }
        // test for expiring
        if(textMessage.contains("Your " + this.listenedKillstreak + " has expired!")) {
            this.isActive = false;
            // update the duration
            this.duration = System.currentTimeMillis() - this.startTime;
            this.renderedString = "";
        }
    }

    @Override
    public String getRenderingString() {
        if(this.renderedString.isEmpty()) {
            // don't draw anything
            return "";
        }
        else {
            return EnumChatFormatting.DARK_PURPLE + this.listenedKillstreak + ": " + renderedString;
        }
    }
    
    private String getColorForTime(long time) {
        if(time >= 10) {
            // green
            return String.valueOf(EnumChatFormatting.GREEN);
        } else if( time >= 5){
            // orange
            return String.valueOf(EnumChatFormatting.GOLD);
        } else {
            // red
            return String.valueOf(EnumChatFormatting.RED);
        }
    }

}
