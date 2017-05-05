package net.unaussprechlich.project.connect.socket.io


enum class EnumSocketEvents (val eventName : String){
    PRELOGIN("prelogin"),
    LOGIN("login");

    override fun toString() : String{
        return this.eventName
    }
}