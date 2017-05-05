package net.unaussprechlich.project.connect.socket.io.requests

import net.unaussprechlich.project.connect.socket.io.EnumSocketEvents
import org.json.JSONObject


class LoginRequest(ack : ((args : JSONObject, success : Boolean) -> (Unit))?) : AbstractRequest(ack) {

    var UUID  = ""
    var NAME  = ""
    var API_KEY = ""
    var PASSWORD = ""

    override fun assembleArgs(obj: JSONObject): JSONObject {
        obj.put("UUID", UUID)
        obj.put("NAME", NAME)
        obj.put("API_KEY", API_KEY)
        obj.put("PASSWORD", PASSWORD)
        return obj
    }

    override fun getEventName(): EnumSocketEvents {
        return EnumSocketEvents.LOGIN
    }


}