/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.components;

import net.minecraft.util.EnumChatFormatting;

public class TimerComponent implements IComponent{
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

    @Override
    public void onGameStart() {
        // save the current time
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
    }

    @Override
    public String getConfigName() {
        return "TimeDisplay";
    }

    @Override
    public String getConfigComment() {
        return "Turn on/off the Time Display for %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }
}
