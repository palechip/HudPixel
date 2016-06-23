package de.unaussprechlich.hudpixelextended.statsviewer.msc;

import com.palechip.hudpixelmod.util.GameType;

import java.util.HashMap;

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
public class StatsCache {

    /**
     * small class to save the loaded stats for future rendering
     */

    private static HashMap<String, HashMap<GameType, IGameStatsViewer>> statsCacheMap
            = new HashMap<String, HashMap<GameType, IGameStatsViewer>>();

    public static boolean containsPlayer(String playerName){
        return statsCacheMap.containsKey(playerName);
    }

    public static IGameStatsViewer getPlayerByName(String playerName, GameType gameType){
        if(statsCacheMap.containsKey(playerName)){
            if(statsCacheMap.get(playerName).containsKey(gameType)){
                return statsCacheMap.get(playerName).get(gameType);
            }
        }

        //when the stats for the given player are not stored create them with the statsfactory
        statsCacheMap.put(playerName, new HashMap<GameType, IGameStatsViewer>());
        statsCacheMap.get(playerName).put(gameType, StatsViewerFactory.getStatsViewerClass(playerName, gameType));

        return statsCacheMap.get(playerName).get(gameType);
    }
}
