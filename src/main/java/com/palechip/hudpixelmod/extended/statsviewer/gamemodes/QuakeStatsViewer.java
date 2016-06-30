package com.palechip.hudpixelmod.extended.statsviewer.gamemodes;

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.stats.StatsDisplayer;

import java.util.ArrayList;

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
 *
 * Class made by skyerzz (so you know who to blame)
 *******************************************************************************/
public class QuakeStatsViewer extends StatsDisplayer implements IGameStatsViewer {


    private ArrayList<String> renderList;
    private int solo_kills;
    private int team_kills;
    private int solo_deaths;
    private int team_deaths;
    private int solo_wins;
    private int team_wins;

    private float trigger;

    private double team_kd;
    private double solo_kd;


    /*
     *Lets add some static finals. Players love static finals.
     */
    private static final String TRIGGER = D_GRAY + " [" + GRAY + "Trigger speed" + D_GRAY + "] ";
    private static final String SOLO_KD = D_GRAY + " [" + GRAY + "Solo K/D" + D_GRAY + "] ";
    private static final String TEAM_KD = D_GRAY + " [" + GRAY + "Team K/D" + D_GRAY + "] ";
    private static final String SOLO_KILLS = D_GRAY + " [" + GRAY + "Solo kills" + D_GRAY + "] ";
    private static final String SOLO_DEATHS = D_GRAY + " [" + GRAY + "Solo deaths" + D_GRAY + "] ";
    private static final String TEAM_KILLS = D_GRAY + " [" + GRAY + "Team kills" + D_GRAY + "] ";
    private static final String TEAM_DEATHS = D_GRAY + " [" + GRAY + "Team deaths" + D_GRAY + "] ";
    private static final String SOLO_WINS = D_GRAY + " [" + GRAY + "Solo wins" + D_GRAY + "] ";
    private static final String TEAM_WINS = D_GRAY + " [" + GRAY + "Team wins" + D_GRAY + "] ";

    public QuakeStatsViewer(String playerName) {
        super(playerName);
        renderList = new ArrayList<String>();
    }

    @Override
    public ArrayList<String> getRenderList() {
        if(renderList.isEmpty()){
            return null;
        }
        return renderList;
    }

    @Override
    protected void displayStats() {
        composeStats();
    }

    private void generateRenderList(){
        renderList.add(SOLO_WINS + GOLD + this.solo_wins + TEAM_WINS + GOLD + this.team_wins);
        renderList.add(TEAM_KILLS + GOLD + this.team_kills + TEAM_DEATHS + GOLD + this.team_deaths + TEAM_KD + GOLD + this.team_kd);
        renderList.add(SOLO_KILLS + GOLD + this.solo_kills + SOLO_DEATHS + GOLD + this.solo_deaths + SOLO_KD + GOLD + this.solo_kd);
        renderList.add(TRIGGER + GOLD + this.trigger);
    }

    public void composeStats() {

        this.solo_kills = getInt("kills");
        this.team_kills = getInt("kills_teams");
        this.solo_deaths = getInt("deaths");
        this.team_deaths = getInt("deaths_teams");
        this.solo_wins = getInt("wins");
        this.team_wins = getInt("wins_teams");

        if(solo_deaths > 0) {
            this.solo_kd = Math.floor((solo_kills / solo_deaths) * 1000) / 1000;
        }
        else{
            this.solo_kd = 1;
        }
        if(this.team_deaths>0) {
            this.team_kd = Math.floor((team_kills / team_deaths) * 1000) / 1000;
        }
        else{
            this.team_kd = 1;
        }

        this.trigger = getTrigger(getString("trigger"));

        generateRenderList();

    }

    private int getInt(String s){
        try{
            return this.statistics.get("Quake").getAsJsonObject().get(s).getAsInt();
        }catch(Exception ex){
            LoggerHelper.logInfo("[Stats.Quake.Int]: No entry for " + s + "returning 0!");
            return 0;
        }
    }

    private String getString(String s) {
        try{
            return this.statistics.get("Quake").getAsJsonObject().get(s).getAsString();
        }catch(Exception ex){
            LoggerHelper.logInfo("[Stats.Quake.String]: No entry for " + s + "returning Err!");
            return "Err";
        }
    }

    private float getTrigger(String trigger)
    {
        switch(trigger) {
            case "NINE_POINT_ZERO":
                return 9.0f;
            case "FIVE_POINT_ZERO":
                return 5.0f;
            case "TWO_POINT_FIVE":
                return 2.5f;
            case "ONE_POINT_FIVE":
                return 1.5f;
            case "ONE_POINT_FOUR":
                return 1.4f;
            case "ONE_POINT_THREE":
                return 1.3f;
            case "ONE_POINT_TWO":
                return 1.2f;
            case "ONE_POINT_ONE":
                return 1.1f;
            case "ONE_POINT_ZERO":
                return 1.0f;
            case "ZERO_POINT_NINE":
                return 0.9f;
            default:
                return 666f;
        }
    }
}
