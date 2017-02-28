/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.Player;

import jline.internal.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * PlayerDatabase Created by Alexander on 27.02.2017.
 * Description:
 **/
public class PlayerDatabase {

    //TODO

    private static HashMap<UUID, PlayerModel> playerDatabase = new HashMap<>();
    private static HashMap<String, UUID>      playerNameUUID = new HashMap<>();

    private static HashMap<UUID, PlayerModelBuilder> currentBuilders = new HashMap<>();

    @Nullable
    public static PlayerModel getPlayerByName(String name, IPlayerModelLoadedCallback callback){
        if(playerNameUUID.containsKey(name)){
            return playerDatabase.get(playerNameUUID.get(name));
        }
        return null;
    }

    public static PlayerModel getPlayerByUUID(UUID uuid, IPlayerModelLoadedCallback callback){
        if(playerDatabase.containsKey(uuid)){
            return playerDatabase.get(uuid);
        }
        return null;
    }
}
