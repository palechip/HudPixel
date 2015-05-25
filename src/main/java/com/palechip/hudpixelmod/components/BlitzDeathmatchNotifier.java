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

public class BlitzDeathmatchNotifier implements IComponent {
    private static final String DISPLAY_STRING = "DEATHMATCH STARTING SOON";
    private static final int ANIMATION_TIME = 5; // in MC-Ticks
    private static final int DISPLAY_TIME = 140; // = 7s

    private boolean isDisplaying;
    private int ticksLeft;
    private String renderingString;


    public BlitzDeathmatchNotifier() {
    }

    @Override
    public void setupNewGame() {
        this.ticksLeft = 0;
        this.isDisplaying = false;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
        this.ticksLeft = 0;
        this.isDisplaying = false;
    }

    @Override
    public void onTickUpdate() {
        if(this.isDisplaying) {
            // count down the ticks
            this.ticksLeft--;
            if(this.ticksLeft <= 0) {
                this.isDisplaying = false;
                this.ticksLeft = 0;
            }

            // update the color
            switch((this.ticksLeft / ANIMATION_TIME) % 4) {
            case 0:
                this.renderingString = EnumChatFormatting.LIGHT_PURPLE + DISPLAY_STRING;
                break;
            case 1:
                this.renderingString = EnumChatFormatting.YELLOW + DISPLAY_STRING;
                break;
            case 2:
                this.renderingString = EnumChatFormatting.GREEN + DISPLAY_STRING;
                break;
            case 3:
                this.renderingString = EnumChatFormatting.RED + DISPLAY_STRING;
                break;
            }

        }
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        if(textMessage.contains("Deathmatch in 45 seconds!") || textMessage.contains("Deathmatch begins in 1 minute!")) {
            this.isDisplaying = true;
            this.ticksLeft = DISPLAY_TIME;
        }
    }

    @Override
    public String getRenderingString() {
        if(this.isDisplaying) {
            return this.renderingString;
        } else {
            return "";
        }
    }

    @Override
    public String getConfigName() {
        return "DeathmatchNotifier";
    }

    @Override
    public String getConfigComment() {
        return "Show/Hide a little flashing message when deathmatch is near in %game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }

}
