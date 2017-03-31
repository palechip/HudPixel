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
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NameResolver(val uuid : UUID, val callback: (NameHistory) -> Unit) : Thread() {

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
        callback.invoke(getRemoteNameFromAPI())
    }

    private fun getRemoteNameFromAPI(): NameHistory {
        try {
            val conn: HttpURLConnection = URL((baseURL + uuid.toString().replace("-", "")  + "/names")).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val content = InputStreamReader(conn.inputStream)

            return NameHistory(Gson().fromJson(content, JsonArray::class.java), false)

        } catch (e: Exception) {
            e.printStackTrace()
            return NameHistory(null, true)
        }
    }
}