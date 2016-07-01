/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package com.palechip.hudpixelmod.extended.boosterdisplay;

import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.extended.util.GameIconLoader;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import com.palechip.hudpixelmod.util.GameType;

public class BoosterExtended extends FancyListObject implements McColorHelper{

    private static final long tipDelay = 30 * 60 * 1000;


    private GameType gameType;
    private long timeNextTip;
    private String countDown;
    private Booster booster;

    public Booster getBooster() {
        return booster;
    }

    public GameType getGameType() {
        return gameType;
    }

    public BoosterExtended(GameType gameType) {
        countDown = "";
        timeNextTip = 0;
        this.gameType = gameType;
        this.resourceLocation = GameIconLoader.gameIconLocation(gameType);
    }

    public void setGameModeTipped(String player){
        timeNextTip = System.currentTimeMillis() + tipDelay;
        if(booster != null && booster.getOwner().equalsIgnoreCase(player)) booster.tip();
    }

    public void setCurrentBooster(Booster booster){
        this.booster = booster;
    }

    @Override
    public void onClientTick(){
        this.renderPicture = countDown();
        this.renderLineSmall = YELLOW + gameType.getName();
        this.renderLine1 = GOLD + gameType.getName();
        if(booster == null){
            this.renderLine2 = GRAY + "No Booster online!";
        } else {
            if(booster.isTipped()){
                this.renderLine2 = RED + booster.getOwner();
            } else {
                this.renderLine2 = GREEN + booster.getOwner();
            }
        }
    }

    private String countDown(){
        if(timeNextTip < System.currentTimeMillis()) {
            timeNextTip = 0;
            return "";
        }
        long timeBuff = timeNextTip - System.currentTimeMillis();
        String sMin;
        long min = (timeBuff/1000/60);
        if(min < 10) sMin = "0" + min;
        else         sMin = ""  + min;
        String sSec;
        long sec = (timeBuff/1000) - (min*60);
        if(sec < 10) sSec = "0" + sec;
        else         sSec = ""  + sec;
        return sMin + ":" + sSec;
    }

}
