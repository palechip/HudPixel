/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player.data;

import com.mojang.realmsclient.gui.ChatFormatting;

/**
 * Rank Created by Alexander on 28.02.2017.
 * Description:
 **/
public class Rank {

    private final String rankName;
    private final ChatFormatting rankColor;
    private final String rankFormatted;

    public Rank(String rankName, String rankFormatted, ChatFormatting rankColor){
        this.rankName = rankName;
        this.rankColor = rankColor;
        this.rankFormatted = rankFormatted + rankColor;
    }

    public String getRankFormatted() {
        return rankFormatted;
    }

    public ChatFormatting getRankColor() {
        return rankColor;
    }

    public String getRankName() {
        return rankName;
    }
}
