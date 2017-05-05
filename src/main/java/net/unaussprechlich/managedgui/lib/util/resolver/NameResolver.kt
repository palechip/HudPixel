/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.resolver

import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NameResolver(val uuid : UUID, val callback: (NameHistory) -> Unit){

    class NameHistory(result : JsonArray?, val failed : Boolean){

        val currentName : String
        val nameMap : MutableMap<Long, String> = mutableMapOf()

        init {
            if(result == null)
                currentName = "!ERROR!"
            else{
                result.map { it.asJsonObject }.forEach { nameMap.put(if(it.get("changedToAt") == null) 0 else it.get("changedToAt").asLong, it.get("name").asString) }
                currentName = nameMap[nameMap.keys.max()] ?: "!ERROR!"
            }
        }
    }

    private val baseURL = "https://api.mojang.com/user/profiles/"

    init {
        asyncGetRemoteNameFromAPI()
    }

    private fun asyncGetRemoteNameFromAPI() = launch(CommonPool){
        try {
            val conn: HttpURLConnection = URL((baseURL + uuid.toString().replace("-", "")  + "/names")).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            callback.invoke( NameHistory(Gson().fromJson(InputStreamReader(conn.inputStream), JsonArray::class.java), false))

        } catch (e: Exception) {
            callback.invoke(NameHistory(null, true))
        }
    }
}