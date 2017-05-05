/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util.resolver

import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class UUIDResolver(val username : String, val callback: (UUID) -> Unit){

    class APIResponse {
        var id: String? = null
        var name: String? = null
    }

    private val baseURL = "https://api.mojang.com/users/profiles/minecraft/"

    init {
        asyncGetRemoteUUIDFromAPI()
    }

    private fun asyncGetRemoteUUIDFromAPI() = launch(CommonPool){
        try {
            val conn: HttpURLConnection = URL(baseURL + username).openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            callback.invoke(UUID.fromString((Gson().fromJson(BufferedReader(InputStreamReader(conn.inputStream))
                    .readLine(), APIResponse::class.java).id)!!.replace("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})".toRegex(), "$1-$2-$3-$4-$5")))

        } catch (e: Exception) {
            callback.invoke(UUID.fromString("00000000-0000-0000-0000-000000000000"))
        }
    }
}


