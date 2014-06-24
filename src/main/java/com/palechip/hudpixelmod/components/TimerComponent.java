package com.palechip.hudpixelmod.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.util.EnumChatFormatting;

public class TimerComponent {
    public static final String TIME_DISPLAY_MESSAGE = EnumChatFormatting.YELLOW + "Time: ";
    private long startTime;
    private String runningTime;

    public String getRenderingString() {
        return TIME_DISPLAY_MESSAGE + runningTime;
    }
    
    public void setupNewGame() {
        // reset
        this.startTime = 0;
        this.runningTime = "00:00";
    }
    
    public void onStartGame() {
        // save the current time
        this.startTime = System.currentTimeMillis();
    }
    
    public void onTickUpdate() {
        // update the time
        // I tried using Date, but it didn't work out as I wanted so I decided to write this myself
        long timeDifferenceSeconds, timeDifferenceMinutes, timeDifferenceHours;
        long timeDifference = System.currentTimeMillis() - this.startTime;
        // the +500 are for proper rounding
        timeDifferenceSeconds = (timeDifference + 500) / 1000;
        timeDifferenceMinutes = timeDifferenceSeconds / 60;
        timeDifferenceHours = timeDifferenceMinutes / 60;
        
        // translate to our format
        this.runningTime = timeDifferenceHours > 0 ? timeDifferenceHours + ":" : "" + ((timeDifferenceMinutes % 60 < 10) ? "0" : "") + timeDifferenceMinutes % 60 + ":" + ((timeDifferenceSeconds % 60 < 10) ? "0" : "") + timeDifferenceSeconds % 60;
    }
}
