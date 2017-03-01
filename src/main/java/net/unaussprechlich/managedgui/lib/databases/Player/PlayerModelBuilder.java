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

public class PlayerModelBuilder {

    private String           name;
    private UUID             uuid;
    private ResourceLocation headResource;
    private IPlayerModelLoadedCallback callback;

    public PlayerModelBuilder setCallback(IPlayerModelLoadedCallback callback) {
        this.callback = callback;
        return this;
    }

    public PlayerModelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerModelBuilder setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public PlayerModelBuilder setHeadResource(ResourceLocation headResource) {
        this.headResource = headResource;
        return this;
    }

    /*
    public PlayerModel create() {
        return new PlayerModel(name, uuid, headResource);
    }*/
}