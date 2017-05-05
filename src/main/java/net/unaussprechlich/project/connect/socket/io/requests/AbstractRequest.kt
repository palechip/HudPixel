package net.unaussprechlich.project.connect.socket.io.requests

import io.socket.client.Ack
import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import net.unaussprechlich.project.connect.socket.io.SocketConnection
import org.json.JSONObject


abstract class AbstractRequest(val ack : ((args : JSONObject , success : Boolean) -> (Unit))?) {

    abstract fun assembleArgs(obj : JSONObject = JSONObject()) : JSONObject
    abstract fun getEventName() : EnumSocketEvents

    private fun getArgs() : JSONObject{
        val args = assembleArgs()
        args.put("SESSION_TOKEN", SocketConnection.SESSION_TOKEN)
        return args
    }

    fun send() {
        if(ack == null) SocketConnection.emit(getEventName(), assembleArgs())
        else SocketConnection.emit(getEventName(), getArgs(), Ack { args ->
            try {
                val obj = args[0] as JSONObject
                val success = obj.getBoolean("success")
                ack.invoke(obj,success )
                if(!success) throw Exception("[Socket] Server responded with Exception! \n REQUEST: ${getEventName()} \n ARGUMENTS: ${assembleArgs()} \n RESPONSE: $obj")
            } catch(e : Exception){
                e.printStackTrace()
            }

        })
    }


}