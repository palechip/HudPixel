package com.palechip.hudpixelmod.components;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;

public class TkrTimer implements IComponent {
    public static final String LAP_COMPLETION_MESSAGE_REGEX = "(Lap \\d Completed!).*";

    private int lap;
    private boolean running = false;
    private long startingTime = 0;
    private String runningTime = "00:00:000";

    private String officialTime = "";

    private static long startDelay = 0l;

    /**
     * Create a new accurate Timer for Turbo Kart Racers
     * @param lap The Lap this timer counts. 0 means all laps are counted.
     */
    public TkrTimer(int lap) {
        this.lap = lap;
    }

    @Override
    public void setupNewGame() {
    }

    @Override
    public void onGameStart() {
        // start the general timer and the first lap timer
        if(this.lap == 0 || this.lap == 1) {
            // add the start delay. Setting the start into the future if we know the start delay
            this.startingTime = System.currentTimeMillis() + this.startDelay;
            this.running = true;
        }
    }

    @Override
    public void onGameEnd() {
        this.running = false;
    }

    @Override
    public void onTickUpdate() {
        if(this.running) {
            // update the time
            long timeDifference = System.currentTimeMillis() - this.startingTime;
            long timeDifferenceSeconds = timeDifference / 1000;
            long timeDifferenceMinutes = timeDifferenceSeconds / 60;

            // translate to our format
            this.runningTime = ((timeDifferenceMinutes % 60 < 10) ? "0" : "") + timeDifferenceMinutes % 60 + ":" + ((timeDifferenceSeconds % 60 < 10) ? "0" : "") + timeDifferenceSeconds % 60 + ":"+  ((timeDifference % 1000 < 100) ? "0" : "") + ((timeDifference % 1000 < 10) ? "0" : "") + timeDifference % 1000;
        }

    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // check if the message is relevant
        if(textMessage.matches(LAP_COMPLETION_MESSAGE_REGEX)) {
            try {
                // the lap number is the 5th character. It needs to be cast to String first because otherwise we get the wrong value
                int lapNo = Integer.valueOf(String.valueOf(textMessage.charAt(4)));
                
                // check if the listened lap was completed
                if(this.lap == lapNo) {
                    // extract the start message
                    this.officialTime = textMessage.substring(textMessage.indexOf('(') + 1, textMessage.indexOf(')'));
                    // stop the timer
                    this.running = false;
                }
                
                // start the next timer
                if(this.lap - 1 == lapNo) {
                    this.running = true;
                    // there is no delay here
                    this.startingTime = System.currentTimeMillis();
                }
                
                // accuracy check after the first lap
                if(lapNo == 1 && (this.lap == 0 || this.lap == 1)) {
                    // save the current time
                    long currentTime = System.currentTimeMillis();
                    // save the measured time
                    long measuredTime = currentTime -  this.startingTime;

                    ArrayList<Integer> officialTime = new ArrayList<Integer>(3);
                    // convert the officialTime
                    for (String s : this.officialTime.split(":")) {
                        officialTime.add(Integer.valueOf(s));
                    }
                    // convert everything into milliseconds
                    long officialTimeMilliSeconds = officialTime.get(0) * 60 * 1000 + officialTime.get(1) * 1000 + officialTime.get(2);
                    
                    // correct the main timer
                    if(this.lap == 0) {
                        this.startingTime = currentTime - officialTimeMilliSeconds;
                    }

                    // the start delay is the difference between our (greater) measured time and the official time
                    this.startDelay = this.startDelay + (measuredTime - officialTimeMilliSeconds);
                }
            } catch (Exception e) {
                e.printStackTrace();
                HudPixelMod.instance().logWarn("Failed to parse the lap completion message. Ignoring");
            }
        }
        // stop the general timer when finishing
        if(textMessage.startsWith(FMLClientHandler.instance().getClient().getSession().getUsername() + " has finished the race at position ") && this.lap == 0) {
            this.running = false;
        }
    }

    @Override
    public String getRenderingString() {
        // for the general timer
        if(this.lap == 0) {
            // show the running time
            return TimerComponent.TIME_DISPLAY_MESSAGE + this.runningTime;
        } else {
            if(this.running) {
                // show the result if the timer is running
                return EnumChatFormatting.YELLOW + "Lap " + this.lap + ": " + this.runningTime;
            } else {
                // return the official time if it isn't running. This may also be empty if it hasn't started yet
                return this.officialTime.isEmpty() ? "" : EnumChatFormatting.YELLOW + "Lap " + this.lap + ": " + this.officialTime;
            }
        }
    }

    @Override
    public String getConfigName() {
        return "AccurateTimeDisplay";
    }

    @Override
    public String getConfigComment() {
        return "Turn on/off the Accurate Time Display for %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }

}
