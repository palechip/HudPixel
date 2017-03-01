package net.unaussprechlich.hypixel.helper;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.unaussprechlich.managedgui.lib.databases.Player.data.Rank;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

/**
 * HYPIXEL_RANK Created by Alexander on 28.02.2017.
 * Description:
 **/
public enum HYPIXEL_RANK {

    DEFAULT("" , "", GRAY),
    VIP("[VIP]" , GREEN + "[VIP]", GREEN),
    VIP_PLUS("[VIP]" , GREEN + "[VIP" + GOLD + "+" + GREEN + "]", GREEN),
    MVP("[MVP]" , AQUA + "[VIP]", AQUA),
    MVP_PLUS("[MVP]" , AQUA + "[VIP" + GOLD + "+" + AQUA + "]", AQUA),
    HELPER("[HELPER]" , BLUE + "[VIP]", BLUE),
    MOD("[MOD]" , DARK_GREEN + "[MOD]", DARK_GREEN),
    YT("[YT]" , GOLD + "[YT]", GOLD),
    ADMIN("[ADMIN]", RED + "[ADMIN]", RED);

    private final Rank rank;

    HYPIXEL_RANK(String name, String nameFormatted, ChatFormatting color){
        this.rank = new Rank(name, nameFormatted, color);
    }

    public Rank get() {
        return rank;
    }
}
