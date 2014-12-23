package com.palechip.hudpixelmod.components;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class PaintballKillstreakTracker implements IComponent {
    private static final String COOLDOWN_SIGN = EnumChatFormatting.RED + "\u2717"; // fancy x
    private static final String ACTIVE_SIGN = EnumChatFormatting.GREEN + "\u2713"; // check mark

    private String renderedString;
    private String listenedKillstreak;
    private boolean isTimed;
    private boolean isActive;
    private long startTime;
    private long duration;
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
        this.renderedString = "";
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
        if(this.isActive) {
            // only if we already measured the length
            if(this.duration > 0) {
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
        if(textMessage.contains(FMLClientHandler.instance().getClient().getSession().getUsername() + " activated " + this.listenedKillstreak)) {
            this.startTime = System.currentTimeMillis();
            if(this.isTimed) {
                this.isActive = true;
            } else if(this.hasCooldown) {
                this.isCoolingDown = true;
            }
        }
        // test for expiring
        if(textMessage.contains("Your " + this.listenedKillstreak + " has expired!")) {
            this.isActive = false;
         // update the duration
            this.duration = System.currentTimeMillis() - this.startTime;
            this.renderedString = "";
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
            return ACTIVE_SIGN +  EnumChatFormatting.DARK_PURPLE + this.listenedKillstreak + ": " + renderedString;
        } else if(this.isCoolingDown) {
            // the listened killstreak will be red because the color from COOLDOWN_SIGN isn't reset
            return COOLDOWN_SIGN + " " + this.listenedKillstreak;
        } else  {
            return "";
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
