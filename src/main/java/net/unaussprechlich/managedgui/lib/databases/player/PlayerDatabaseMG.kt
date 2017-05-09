/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.player

import net.unaussprechlich.managedgui.lib.util.resolver.NameResolver
import net.unaussprechlich.managedgui.lib.util.resolver.UUIDResolver
import java.util.*

/**
 * PlayerDatabaseMG Created by Alexander on 27.02.2017.
 * Description:
 */
object PlayerDatabaseMG {

    private val playerDatabase = HashMap<UUID, PlayerModel>()
    private val playerNameUUID = HashMap<String, UUID>()

    private fun load(name : String , callback : (PlayerModel) -> Unit){
        UUIDResolver(name){ uuid ->
            load(uuid, callback)
        }
    }

    private fun load(uuid : UUID , callback : (PlayerModel) -> Unit){
        NameResolver(uuid){ name ->
            playerNameUUID.put(name.currentName, uuid)
            playerDatabase.put(uuid,PlayerModel(name, uuid))
            callback.invoke(playerDatabase[uuid] ?: throw Exception("[PlayerDatabase] this is not good :3" ))
        }
    }

    fun get(name: String) : PlayerModel{
        return playerDatabase[playerNameUUID[name]] ?: throw Exception("[PlayerDatabase] player $name is not loaded yet!")
    }

    fun get(uuid: UUID) : PlayerModel{
        return playerDatabase[uuid] ?: throw Exception("[PlayerDatabase] player $uuid is not loaded yet!")
    }


    fun get(name: String, callback : (PlayerModel) -> Unit){
        if(!contains(name)){
            load(name, callback)
        } else
            callback.invoke(playerDatabase[playerNameUUID[name]] ?: throw Exception("[PlayerDatabase] this is not good :3"))
    }

    fun get(uuid: UUID, callback: (PlayerModel) -> Unit){
        if(!contains(uuid)){
            load(uuid, callback)
        } else
            callback.invoke(playerDatabase[uuid] ?: throw Exception("[PlayerDatabase] this is not good :3"))
    }

    fun contains(name : String): Boolean{
        return playerNameUUID.containsKey(name)
    }

    fun contains(name : UUID): Boolean{
        return playerDatabase.containsKey(name)
    }



}
