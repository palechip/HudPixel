package com.palechip.hudpixelmod.extended.statsviewer.gamemodes;

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.stats.StatsDisplayer;

import java.util.ArrayList;

/**
 * Created by sky on 30-6-2016.
 */
public class WallsStatsViewer extends StatsDisplayer implements IGameStatsViewer {


    private ArrayList<String> renderList;
    private int kills;
    private int deaths;
    private int wins;
    private int losses;

    private double kd;
    private double wl;

    /*
    *Lets add some static finals. Players love static finals.
    */
    private static final String KILLS = D_GRAY + " [" + GRAY + "⚔" + D_GRAY + "] ";
    private static final String DEATHS = D_GRAY + " [" + GRAY + "☠" + D_GRAY + "] ";
    private static final String WINS = D_GRAY + " [" + GRAY + "Wins" + D_GRAY + "] ";
    private static final String LOSSES = D_GRAY + " [" + GRAY + "Losses" + D_GRAY + "] ";
    private static final String KD = D_GRAY + " [" + GRAY + "K/D" + D_GRAY + "] ";
    private static final String WL = D_GRAY + " [" + GRAY + "W/L" + D_GRAY + "] ";


    public WallsStatsViewer(String playerName) {
        super(playerName);
        renderList = new ArrayList<String>();
    }

    @Override
    public ArrayList<String> getRenderList() {
        return renderList;
    }

    @Override
    protected void displayStats() {
        composeStats();
    }

    private void generateRenderList() {
        renderList.add(WINS + GOLD + wins + LOSSES + GOLD + losses + WL + GOLD + wl);
        renderList.add(KILLS + GOLD + kills + DEATHS + GOLD + deaths + KD + GOLD + kd);
    }

    public void composeStats() {

        this.kills = getInt("kills");
        this.deaths = getInt("deaths");
        this.wins = getInt("wins");
        this.losses = getInt("losses");

        if (deaths > 0) {
            this.kd = Math.floor((kills / deaths) * 1000) / 1000;
        } else {
            this.kd = 1;
        }

        if (losses > 0) {
            this.wl = Math.floor((kills / deaths) * 1000) / 1000;
        } else {
            this.wl = 1;
        }

        generateRenderList();

    }

    private int getInt(String s) {
        try {
            return this.statistics.get("Walls").getAsJsonObject().get(s).getAsInt();
        } catch (Exception ex) {
            LoggerHelper.logInfo("[Stats.Walls.Int]: No entry for " + s + " returning 0!");
            return 0;
        }
    }
}
