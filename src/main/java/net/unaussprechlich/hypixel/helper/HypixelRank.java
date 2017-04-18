package net.unaussprechlich.hypixel.helper;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.unaussprechlich.managedgui.lib.databases.player.data.Rank;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

/**
 * HypixelRank Created by Alexander on 28.02.2017.
 * Description:
 **/
public enum HypixelRank {

    DEFAULT("" , "", GRAY),
    VIP("[VIP]" , GREEN + "[VIP]", GREEN),
    VIP_PLUS("[VIP+]" , GREEN + "[VIP" + GOLD + "+" + GREEN + "]", GREEN),
    MVP("[MVP]" , AQUA + "[MVP]", AQUA),
    MVP_PLUS("[MVP+]" , AQUA + "[MVP" + GOLD + "+" + AQUA + "]", AQUA),
    HELPER("[HELPER]" , BLUE + "[HELPER]", BLUE),
    MOD("[MOD]" , DARK_GREEN + "[MOD]", DARK_GREEN),
    YT("[YT]" , GOLD + "[YT]", GOLD),
    ADMIN("[ADMIN]", RED + "[ADMIN]", RED);

    private final Rank rank;



    HypixelRank(String name, String nameFormatted, ChatFormatting color){
        this.rank = new Rank(name, nameFormatted, color);
    }

    public static Rank getRankByName(String name){

        for (HypixelRank hypixelRank : HypixelRank.values()) {
            if(hypixelRank.name().equals(name)){
                System.out.print(hypixelRank.get().getRankName());
                return hypixelRank.get();
            }
        }
        return DEFAULT.get();
    }

    public Rank get() {
        return rank;
    }
}
