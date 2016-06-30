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
 *******************************************************************************/
public class SkywarsStatsViewer extends StatsDisplayer implements IGameStatsViewer {

    private int kills;
    private int assists;
    private int deaths;
    private double kd;
    private int wl;
    private int losses;
    private int wins;
    private int shamanLevel;
    private int warriorLevel;
    private int mageLevel;
    private int paladinLevel;
    private ArrayList<String> renderList;

    /**
     * Some static-final stuff to make the code cleaner ....
     */
    private static final String SHA =  D_GRAY + " [" + WHITE + "SHA" + D_GRAY + "] ";
    private static final String WAR =  D_GRAY + " [" + WHITE + "WAR" + D_GRAY + "] ";
    private static final String PAL =  D_GRAY + " [" + WHITE + "PAL" + D_GRAY + "] ";
    private static final String MAG =  D_GRAY + " [" + WHITE + "MAG" + D_GRAY + "] ";
    private static final String KDA =  D_GRAY + " [" + WHITE + "K"   + D_GRAY + "|"
                          + WHITE + "A" + D_GRAY + "|" + WHITE + "D" + D_GRAY + "] ";
    private static final String LOS =  D_GRAY + " [" + WHITE + "LOS" + D_GRAY + "] ";
    private static final String WIN =  D_GRAY + " [" + WHITE + "WIN" + D_GRAY + "] ";
    private static final String KD  =  D_GRAY + " [" + WHITE + "K/D" + D_GRAY + "] ";
    private static final String WL  =  D_GRAY + " [" + WHITE + "Wl"  + D_GRAY + "] ";

    /**
     * constructor for the StatsViewerFactory
     * @param playerName name of the Player
     */
    public SkywarsStatsViewer(String playerName) {
        super(playerName);
        renderList = new ArrayList<String>();
    }

    /**
     * Implements the IGameStatsViewer interface
     * @return the renderList
     */
    @Override
    public ArrayList<String> getRenderList(){
        if(renderList.isEmpty()){
           return null;
        }
        return renderList;
    }

    /**
     * generates the renderList
     */
    private void generateRenderList(){
        renderList.add(KD + GOLD + kd + WL + GOLD + wl +"%");
        renderList.add(LOS + GOLD + losses + WIN + GOLD + wins);
        renderList.add(KDA + GOLD + kills + D_GRAY + " | " + GOLD + assists + D_GRAY + " | " + GOLD + deaths );
        renderList.add(SHA + GOLD + shamanLevel + WAR + GOLD + warriorLevel + PAL + GOLD + paladinLevel + MAG + GOLD + mageLevel);
    }

    /**
     * Function to compose the Jason object into teh right variables
     */
    private void composeStats() {

        kills =     getInt("kills");
        assists =   getInt("assists");
        deaths =    getInt("deaths");
        wins =      getInt("wins");
        losses =    getInt("losses");

        kd = (double) Math.round(((double)kills/(double)deaths) * 100) / 100;
        wl = (int) Math.round(((double)wins / (double)(wins+losses)) * 100);

        shamanLevel = getInt("shaman_health")    + getInt("shaman_energy")              + getInt("shaman_cooldown")
                + getInt("shaman_critchance")    + getInt("shaman_critmultiplier")      + getInt("shaman_skill1")
                + getInt("shaman_skill2")        + getInt("shaman_skill3")              + getInt("shaman_skill4")
                + getInt("shaman_skill5");

        warriorLevel = getInt("warrior_health")  + getInt("warrior_energy")             + getInt("warrior_cooldown")
                + getInt("warrior_critchance")   + getInt("warrior_critmultiplier")     + getInt("warrior_skill1")
                + getInt("warrior_skill2")       + getInt("warrior_skill3")             + getInt("warrior_skill4")
                + getInt("warrior_skill5");

        mageLevel = getInt("mage_health")        + getInt("mage_energy")                + getInt("mage_cooldown")
                + getInt("mage_critchance")      + getInt("mage_critmultiplier")        + getInt("mage_skill1")
                + getInt("mage_skill2")          + getInt("mage_skill3")                + getInt("mage_skill4")
                + getInt("mage_skill5");

        paladinLevel = getInt("paladin_health")  + getInt("paladin_energy")             + getInt("paladin_cooldown")
                + getInt("paladin_critchance")   + getInt("paladin_critmultiplier")     + getInt("paladin_skill1")
                + getInt("paladin_skill2")       + getInt("paladin_skill3")             + getInt("paladin_skill4")
                + getInt("paladin_skill5");

        generateRenderList();
    }

    /**
     * little helper function that gets the Json entry
     * @param s entry in the "Warlords" object
     * @return 0 if the entry is null
     */
    private int getInt(String s){
        try{
            return this.statistics.get("Battleground").getAsJsonObject().get(s).getAsInt();
        }catch(Exception ex){
            LoggerHelper.logInfo("[Stats]: No entry for " + s + "returning 0!");
            return 0;
        }
    }

    /**
     * given by the abstract superclass
     */
    @Override
    protected void displayStats() {
        composeStats();
    }
}
