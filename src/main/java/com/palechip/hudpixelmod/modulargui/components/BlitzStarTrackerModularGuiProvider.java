package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.util.EnumChatFormatting;

public class BlitzStarTrackerModularGuiProvider extends HudPixelModularGuiProvider {

    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.BLITZ);
    }

    private enum Phase {NOT_RELEASED, HIDDEN, FOUND, ACTIVE, USED, FORFEIT}

    ;
    public static final String DISPLAY_MESSAGE = EnumChatFormatting.DARK_GREEN + "Blitz Star";

    private Phase currentPhase;
    private String owner;
    private String activatedBlitz;
    private final long DURATION = 60000; // = 60s The blitz star ability only lasts 30s. It's intentionally inaccurate.
    private long startTime;

    @Override
    public void setupNewGame() {
        this.currentPhase = Phase.NOT_RELEASED;
        this.activatedBlitz = "";
        this.owner = "";
        this.startTime = 0;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onTickUpdate() {
        // update the time when active
        if (this.currentPhase == Phase.ACTIVE) {
            // expired?
            if (System.currentTimeMillis() - startTime >= DURATION) {
                this.currentPhase = Phase.USED;
            }

        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // filter chat tag
        textMessage = textMessage.replace("[" + HudPixelMod.instance().gameDetector.getCurrentGame().getConfiguration().getChatTag() + "]: ", "");
        // hide message
        if (textMessage.contains("The Blitz Star has been hidden in a random chest!")) {
            this.currentPhase = Phase.HIDDEN;
        }
        // somebody found it.
        else if (textMessage.contains("found the Blitz Star!")) {
            this.owner = textMessage.substring(0, textMessage.indexOf(' ') + 1).replace(" ", "");
            this.currentPhase = Phase.FOUND;
        }
        // the holder was killed
        else if (textMessage.contains(this.owner + " was killed")) {
            this.owner = ""; // we could find the killer but it isn't done intentionally.
        }
        // somebody used it
        else if (textMessage.contains(" BLITZ! ")) {
            // update the owner
            this.owner = textMessage.substring(0, textMessage.indexOf(' ')).replace(" ", "");
            this.startTime = System.currentTimeMillis();
            this.activatedBlitz = textMessage.substring(textMessage.indexOf('!') + 2).replace(" ", "");
            this.currentPhase = Phase.ACTIVE;
        }
        // it's too close before deathmatch
        else if (this.currentPhase != Phase.USED && this.currentPhase != Phase.ACTIVE && textMessage.contains("The Blitz Star has been disabled!")) {
            this.currentPhase = Phase.FORFEIT;
        }
    }


    public String getRenderingString() {
        if (currentPhase == null)
            return "";
        switch (this.currentPhase) {
            case NOT_RELEASED:
                // display nothing
                return "Not released";
            case HIDDEN:
                // it's hidden
                return EnumChatFormatting.YELLOW + "Hidden";
            case FOUND:
                // tell the player who had it.
                return EnumChatFormatting.LIGHT_PURPLE + (this.owner.isEmpty() ? "Found" : this.owner);
            case ACTIVE:
                return EnumChatFormatting.RED + this.owner + " -> " + this.activatedBlitz;
            case FORFEIT:
            case USED:
                // it's gone
                return EnumChatFormatting.GREEN + "Gone";
        }
        return "";
    }

    @Override
    public boolean showElement() {
        return doesMatchForGame() && !GameDetector.isLobby();
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }
}
