package net.unaussprechlich.project.connect.chat

import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefChatMessageContainer
import java.util.*


class Chat(private val _id : UUID) {

    private val unprosesedMessages : ArrayList<Message> = arrayListOf()
    private val messageBuffers : HashMap<Int, MessageBuffer> = hashMapOf()

    fun getLast100MessagesAsContainer(){
        var i = 0
        var result : ArrayList<DefChatMessageContainer> = arrayListOf()




    }



}