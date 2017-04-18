/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.databases.player.requests

import net.unaussprechlich.managedgui.lib.databases.player.PlayerDatabaseMG
import java.util.*

/**
 * AbstractPlayerRequest Created by Alexander on 30.03.2017.
 * Description:
 */
class PlayerRequest<T, V>  (val player : T , val callback: (V) -> Unit){


    val isUUID : Boolean


    fun call(data : V) {callback.invoke(data)}

    init {
        if(player !is UUID || player !is String)
            throw IllegalArgumentException("[PlayerRequest] Wrong player parameter type!")

        isUUID = player is UUID

        if(checkDatabase()){

        }

    }

    private fun checkDatabase() : Boolean{
        if(isUUID) return PlayerDatabaseMG.contains(player as UUID)
        else return  PlayerDatabaseMG.contains(player as String)
    }

    fun request(){
        //if(player is UUID)
    }




}
