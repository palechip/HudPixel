package com.palechip.hudpixelmod.components;

import net.minecraft.util.EnumChatFormatting;

public class ArenaPowerupHighlighter implements IComponent {
    private String listenedPowerup;
    private EnumChatFormatting color;
    private boolean isShown;

    public ArenaPowerupHighlighter(String powerup, EnumChatFormatting color) {
        this.listenedPowerup = powerup;
        this.color = color;
    }

    @Override
    public void setupNewGame() {
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        this.isShown = false;
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // check for spawning
        if(textMessage.contains("The " + this.listenedPowerup + " PowerUp has spawned!")) {
            this.isShown = true;
        }
        // check for picking up
        if(textMessage.contains("activated the "+ this.listenedPowerup + " powerup!") || textMessage.contains("You activated the " + this.listenedPowerup + " powerup!")) {
            this.isShown = false;
        }
    }

    @Override
    public String getRenderingString() {
        if(this.isShown) {
            return this.color + this.listenedPowerup;
        } else {
            return "";
        }
    }

}
