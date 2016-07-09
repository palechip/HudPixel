package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.SimpleHudPixelModularGuiProvider;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.HashMap;

public class PaintballKillstreakTrackerModularGuiProvider extends SimpleHudPixelModularGuiProvider {
    @Override
    public boolean doesMatchForGame(Game game) {
        return game.getConfiguration().getDatabaseName().equals("Paintball");
    }

    private static final String COOLDOWN_SIGN = EnumChatFormatting.RED + "\u2717"; // fancy x
    private static final String ACTIVE_SIGN = EnumChatFormatting.GREEN + "\u2713"; // check mark

    private static HashMap<String, PaintballKillstreakTrackerModularGuiProvider> cooldownDependantKillstreaks = new HashMap<String, PaintballKillstreakTrackerModularGuiProvider>();
    private static HashMap<String, Long> durationStorage = new HashMap<String, Long>();

    private String renderedString;
    private String listenedKillstreak;
    private boolean isTimed;
    private boolean isActive;
    private long startTime;
    private long duration;
    private boolean hasCooldown = false;
    private boolean isCoolingDown;

    private String cooldownDependantKillstreak;

    /**
     * Setup a Timer for a specific killstreak
     * @param killstreak The listened killsteak
     * @param isTimed True if the killstreak has a duration, otherwise it'll enter cooldown state upon activation
     * @param hasCooldown True if there is a cooldown for the usage of the killstreak
     * @param cooldownDependantKillstreak Specifys that this killstreak also enters cooldown when the given killstreak is cooling down
     */
    public PaintballKillstreakTrackerModularGuiProvider(String killstreak, boolean isTimed, boolean hasCooldown, String cooldownDependantKillstreak) {
        this.listenedKillstreak = killstreak;
        this.isTimed = isTimed;
        // look if we have saved a duration value from past instances
        if(this.isTimed && durationStorage.containsKey(this.listenedKillstreak)) {
            // set it so we don't have to measure again
            this.duration = durationStorage.get(this.listenedKillstreak);
        }
        this.hasCooldown = hasCooldown;
        // add the string even when it is empty. It's better than null
        this.cooldownDependantKillstreak = cooldownDependantKillstreak;
        if (!cooldownDependantKillstreak.isEmpty()) {
            // if there is a killstreak which this depends on, there also is one which depends on this.
            // for this reason we add this to the list of cooldownDependantKillstreas
            cooldownDependantKillstreaks.put(this.listenedKillstreak, this);
        }
    }
    public PaintballKillstreakTrackerModularGuiProvider() { System.out.println("PB killstreak tracker not implemented!"); }

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
            // and save it for future instances
            durationStorage.put(this.listenedKillstreak, this.duration);
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

    public String getRenderingString() {
        return "";
        /*
        if(this.isActive) {
            return ACTIVE_SIGN +  EnumChatFormatting.DARK_PURPLE + this.listenedKillstreak + ": " + renderedString;
        } else if(this.isCoolingDown ||
                ((!this.cooldownDependantKillstreak.isEmpty() && cooldownDependantKillstreaks.containsKey(this.cooldownDependantKillstreak)) && cooldownDependantKillstreaks.get(this.cooldownDependantKillstreak).isCoolingDown)) {
            // the listened killstreak will be red because the color from COOLDOWN_SIGN isn't reset
            return COOLDOWN_SIGN + " " + this.listenedKillstreak;
        } else  {
            return "";
        }*/
    }

    private String getColorForTime(long time) {
        if(time >= 10) {
            // green
            return String.valueOf(EnumChatFormatting.GREEN);
        } else if( time >= 5){
            // orange
            return String.valueOf(EnumChatFormatting.GOLD);
        } else {
            // red and bold
            return String.valueOf(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD);
        }
    }


    @Override
    public boolean showElement() {
        return doesMatchForGame(HudPixelMod.instance().gameDetector.getCurrentGame());
    }

    @Override
    public String content() {
        return getRenderingString();
    }
}
