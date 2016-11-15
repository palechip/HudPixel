package com.palechip.hudpixelmod.extended.data.player;

import com.palechip.hudpixelmod.extended.util.ILoadPlayerHeadCallback;
import com.palechip.hudpixelmod.extended.util.LoadPlayerHead;
import com.palechip.hudpixelmod.util.UuidCallback;
import com.palechip.hudpixelmod.util.UuidHelper;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class PlayerFactory implements UuidCallback, ILoadPlayerHeadCallback {

    private java.util.UUID uuid;
    private String name;
    private ResourceLocation resourceLocation;
    private IPlayerLoadedCallback iPlayerLoadedCallback;


    public PlayerFactory(UUID uuid, IPlayerLoadedCallback iPlayerLoadedCallback) {
        if(PlayerDatabase.containsUUID(uuid)){
            iPlayerLoadedCallback.onPlayerLoadedCallback(uuid);
            return;
        }
        this.uuid = uuid;
        this.iPlayerLoadedCallback = iPlayerLoadedCallback;
        new UuidHelper(getUUIDasString(), this);
    }

    private String getUUIDasString() {
        return uuid.toString().replace("-", "");
    }


    private void createPlayer() {
        PlayerDatabase.add(new Player(uuid, name, resourceLocation), iPlayerLoadedCallback);
    }

    @Override
    public void onUuidCallback(String playerName) {
        this.name = playerName;
        new LoadPlayerHead(name, this);
    }

    @Override
    public void onLoadPlayerHeadResponse(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        createPlayer(); //player will be created now
    }
}