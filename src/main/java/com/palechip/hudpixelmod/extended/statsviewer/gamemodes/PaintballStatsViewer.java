package com.palechip.hudpixelmod.extended.statsviewer.gamemodes;

import com.palechip.hudpixelmod.extended.statsviewer.msc.IGameStatsViewer;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.stats.StatsDisplayer;

import java.util.ArrayList;

/**
 * Created by sky on 30-6-2016.
 */
public class PaintballStatsViewer extends StatsDisplayer implements IGameStatsViewer {

    //lets get some variables in here
    private ArrayList<String> renderList;
    private int kills;
    private int deaths;
    private int wins;
    private int shots;
    private int endurance, godfather, fortune, headstart, superluck;

    private double kd;

    /*
     *Lets add some static finals. Players love static finals.
     */
    private static final String KILLS = D_GRAY + " [" + GRAY + "⚔" + D_GRAY + "] ";
    private static final String DEATHS = D_GRAY + " [" + GRAY + "☠" + D_GRAY + "] ";
    private static final String WINS = D_GRAY + " [" + GRAY + "Wins" + D_GRAY + "] ";
    private static final String KD = D_GRAY + " [" + GRAY + "K/D" + D_GRAY + "] ";
    private static final String SHOTS = D_GRAY + " [" + GRAY + "Shots" + D_GRAY + "] ";
    private static final String ENDURANCE = D_GRAY + " [" + GRAY + "E" + D_GRAY + "] ";
    private static final String GODFATHER = D_GRAY + " [" + GRAY + "G" + D_GRAY + "] ";
    private static final String FORTUNE = D_GRAY + " [" + GRAY + "F" + D_GRAY + "] ";
    private static final String HEADSTART = D_GRAY + " [" + GRAY + "H" + D_GRAY + "] ";
    private static final String SUPERLUCK = D_GRAY + " [" + GRAY + "L" + D_GRAY + "] ";


    public PaintballStatsViewer(String playerName) {
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
        renderList.add(ENDURANCE + GOLD + endurance + FORTUNE + GOLD + fortune + HEADSTART + GOLD + headstart + SUPERLUCK + GOLD + superluck + GODFATHER + GOLD + godfather);
        renderList.add(KILLS + GOLD + kills + DEATHS + GOLD + deaths + KD + GOLD + kd);
        renderList.add(WINS + GOLD + wins + SHOTS + GOLD + shots);
    }

    public void composeStats() {

        this.kills = getInt("kills");
        this.deaths = getInt("deaths");
        this.shots = getInt("shots_fired");
        this.wins = getInt("wins");
        this.endurance = getInt("endurance");
        this.godfather = getInt("godfather");
        this.fortune = getInt("fortune");
        this.superluck = getInt("superluck");
        this.headstart = getInt("headstart");

        if (deaths > 0) {
            this.kd = Math.floor((kills / deaths) * 1000) / 1000;
        } else {
            this.kd = 1;
        }

        generateRenderList();

    }

    private int getInt(String s) {
        try {
            return this.statistics.get("Paintball").getAsJsonObject().get(s).getAsInt();
        } catch (Exception ex) {
            LoggerHelper.logInfo("[Stats.Paintball.Int]: No entry for " + s + " returning 0!");
            return 0;
        }
    }
}
