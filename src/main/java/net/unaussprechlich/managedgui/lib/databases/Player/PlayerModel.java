/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player;

import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.databases.Player.data.Rank;

import java.util.UUID;

/**
 * PlayerModel Created by Alexander on 27.02.2017.
 * Description:
 **/
public class PlayerModel {

    private final String name;
    private final Rank rank;
    private final UUID uuid;
    private final ResourceLocation headResource;

    public PlayerModel(String name, UUID uuid, Rank rank, ResourceLocation headResource) {
        this.name = name;
        this.uuid = uuid;
        this.rank = rank;
        this.headResource = headResource;
    }

    public String getRankName(){
        if(rank.getRankFormatted().equalsIgnoreCase(rank.getRankColor().toString())) return rank.getRankColor() + name;
        return rank.getRankFormatted() + " " +  name;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ResourceLocation getHeadResource() {
        return headResource;
    }
}
