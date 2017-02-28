/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player;

import net.minecraft.util.ResourceLocation;

import java.util.UUID;

/**
 * PlayerModel Created by Alexander on 27.02.2017.
 * Description:
 **/
public class PlayerModel {

    private final String name;
    private final UUID uuid;
    private final ResourceLocation headResource;

    public PlayerModel(String name, UUID uuid, ResourceLocation headResource) {
        this.name = name;
        this.uuid = uuid;
        this.headResource = headResource;
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
