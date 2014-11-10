package com.palechip.hudpixelmod.components;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;

public class PaintballKillstreakTracker implements IComponent {
    private static final String COOLDOWN_SIGN = EnumChatFormatting.RED + "\u2717"; // fancy x
    private static final String ACTIVE_SIGN = EnumChatFormatting.GREEN + "\u2713"; // check mark

    private String listenedKillstreak;
    private boolean isTimed;
    private boolean isActive;
    private boolean hasCooldown;
    private boolean isCoolingDown;


    /**
     * Setup a Timer for a specific killstreak
     * @param killstreak The listened killsteak
     * @param isTimed True if the killstreak has a duration, otherwise it'll enter cooldown state upon activation
     * @param hasCooldown True if there is a cooldown for the usage of the killstreak
     */
    public PaintballKillstreakTracker(String killstreak, boolean isTimed, boolean hasCooldown) {
        this.listenedKillstreak = killstreak;
        this.isTimed = isTimed;
        this.hasCooldown = hasCooldown;
    }

    @Override
    public void setupNewGame() {
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        this.isActive = false;
        this.isCoolingDown = false;
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // test for starting
        if(textMessage.contains(FMLClientHandler.instance().getClient().getSession().getUsername() + " activated " + this.listenedKillstreak)) {
            if(this.isTimed) {
                this.isActive = true;
            } else if(this.hasCooldown) {
                this.isCoolingDown = true;
            }
        }
        // test for expiring
        if(textMessage.contains("Your " + this.listenedKillstreak + " has expired!")) {
            this.isActive = false;
            if(this.hasCooldown) {
                // Start the cooldown
                this.isCoolingDown = true;
            }
        }
        // test if the cooldown is over
        if(textMessage.contains("You can now use " + this.listenedKillstreak + " again!")) {
            this.isCoolingDown = false;
        }
    }

    @Override
    public String getRenderingString() {
        if(this.isActive) {
            return ACTIVE_SIGN +  EnumChatFormatting.DARK_PURPLE + this.listenedKillstreak;
        } else if(this.isCoolingDown) {
            // the listened killstreak will be red because the color from COOLDOWN_SIGN isn't reset
            return COOLDOWN_SIGN + " " + this.listenedKillstreak;
        } else  {
            return "";
        }
    }

}
