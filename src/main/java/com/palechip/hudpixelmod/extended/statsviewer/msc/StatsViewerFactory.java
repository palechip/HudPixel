package com.palechip.hudpixelmod.extended.statsviewer.msc;

import com.palechip.hudpixelmod.extended.statsviewer.gamemodes.*;
import com.palechip.hudpixelmod.util.GameType;

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
public class StatsViewerFactory {

    public StatsViewerFactory(){}

    /**
     * Not really a factory ... but at least it generates and returns teh right class
     * for the given gametype ... i will maybe switch to a real design pattern
     * @param playerName the playername
     * @param gameType the gametype
     * @return the right statsViewer for the given player and gametype
     */

    public static IGameStatsViewer getStatsViewerClass(String playerName, GameType gameType){

        if(gameType == GameType.WARLORDS){
           return new WarlordsStatsViewer(playerName);
        }
        if(gameType == GameType.QUAKECRAFT){
            return new QuakeStatsViewer(playerName);
        }
        if(gameType == GameType.PAINTBALL){
            return new PaintballStatsViewer(playerName);
        }
        if(gameType == GameType.THE_WALLS){
            return new WallsStatsViewer(playerName);
        }

        return new nullStatsViewerI();
    }


}
