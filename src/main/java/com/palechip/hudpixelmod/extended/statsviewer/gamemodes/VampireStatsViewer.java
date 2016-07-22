package com.palechip.hudpixelmod.extended.statsviewer.gamemodes;

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.stats.StatsDisplayer;

import java.util.ArrayList;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p/>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p/>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * <p/>
 * Class "made" by hst on 22.07.16
 *******************************************************************************/
public class VampireStatsViewer extends StatsDisplayer implements IGameStatsViewer {


    private ArrayList<String> renderList;
    private int coins;
    private int zombie_kills;
    private int vampire_deaths;
    private int vampire_kills;
    private int human_deaths;
    private int human_kills;

    /*
    *Lets add some static finals. Players love static finals.
    */
    private static final String COINS = D_GRAY + " [" + GRAY + "Coins" + D_GRAY + "] ";
    private static final String ZOMBIE_KILLS = D_GRAY + " [" + GRAY + "Zombie Kills" + D_GRAY + "] ";
    private static final String VAMPIRE_DEATHS = D_GRAY + " [" + GRAY + "Deaths" + D_GRAY + "] ";
    private static final String VAMPIRE_KILLS = D_GRAY + " [" + GRAY + "Vampire Kills" + D_GRAY + "] ";
    private static final String HUMAN_DEATHS = D_GRAY + " [" + GRAY + "Deaths" + D_GRAY + "] ";
    private static final String HUMAN_KILLS = D_GRAY + " [" + GRAY + "Human Kills" + D_GRAY + "] ";


    public VampireStatsViewer(String playerName) {
        super(playerName);
        renderList = new ArrayList<String>();
    }

    @Override
    public ArrayList<String> getRenderList() {
        if (renderList.isEmpty()) {
            return null;
        }
        return renderList;
    }

    @Override
    protected void displayStats() {
        composeStats();
    }

    private void generateRenderList() {

        renderList.add(COINS + GOLD + this.coins + ZOMBIE_KILLS + GOLD + this.zombie_kills);
        renderList.add(VAMPIRE_KILLS + GOLD + this.vampire_kills + VAMPIRE_DEATHS + GOLD + this.vampire_deaths);
        renderList.add(HUMAN_KILLS + GOLD + this.human_kills + HUMAN_DEATHS + GOLD + this.human_deaths);
    }

    public void composeStats() {

        this.coins = getInt("coins");
        this.zombie_kills = getInt("zombie_kills");
        this.vampire_deaths = getInt("vampire_deaths");
        this.vampire_kills = getInt("vampire_kills");
        this.human_deaths = getInt("human_deaths");
        this.human_kills = getInt("human_kills");

        generateRenderList();

    }

    private int getInt(String s) {
        try {
            return this.statistics.get("VampireZ").getAsJsonObject().get(s).getAsInt();
        } catch (Exception ex) {
            LoggerHelper.logInfo("[Stats.VampireZ.Int]: No entry for " + s + "returning 0!");
            return 0;
        }
    }
}
