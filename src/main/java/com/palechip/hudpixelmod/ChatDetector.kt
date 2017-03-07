package com.palechip.hudpixelmod

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.unaussprechlich.hypixel.helper.HypixelRank

/**
 * Created by Elad on 3/2/2017.
 */
object ChatDetector {
    fun <T : ChatEventBase<T>> registerEventHandler(event: ChatEventBase<T>, lambda: ChatDetector.(EventInfo<*>) -> Unit) = eventHandlers.put(event, lambda)
    inline fun <reified T> cast(any: Any) = any as T

    abstract class ChatEventBase<T : ChatEventBase<T>> {
        init {
            registerEventBase(this)
        }

        abstract val pattern: Regex
        abstract fun handle(message: String): EventInfo<T>
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun chat(chat: ClientChatReceivedEvent) = events.filter { it.pattern.matches(chat.message.unformattedText) }.map { it.handle(chat.message.unformattedText) }.forEach { eventHandlers.filter { a -> it.type == a.key.javaClass }.forEach { a -> val lambda = a.value; this.lambda(it) } }

    val events = mutableListOf<ChatEventBase<*>>()
    val eventHandlers = mutableMapOf<ChatEventBase<*>, ChatDetector.(EventInfo<*>) -> Unit>()
    private fun registerEventBase(chatEventBase: ChatEventBase<*>) = events.add(chatEventBase)

    /**
     * Use to intercept private messages
     */
    object PrivateMessage : ChatEventBase<PrivateMessage>() {
        override fun handle(message: String): EventInfo<PrivateMessage> {

            val type = if (message.startsWith("To")) "To" else "From"
            var rank = HypixelRank.DEFAULT
            enumValues<HypixelRank>().forEach {
                if (it.get().rankName in message) rank = it
            }
            val name = message.substring((if (type == "To") (message.indexOf("To ") + 3) else (message.indexOf("From ") + 5))..message.indexOf(":")).replace(rank.get().rankName, "")
            val msg = message.substringAfter(": ")
            return EventInfo(PrivateMessage.javaClass, mapOf(
                    "name" to name,
                    "message" to msg,
                    "type" to type,
                    "rank" to rank.name
            ))
        }

        override val pattern: Regex
            get() = "To\\s+.*:\\s+.*|From\\s+.*:\\s+.*".toRegex()
    }

    /**
     * Use to intercept guild messages
     */
    object GuildChat : ChatEventBase<GuildChat>() {
        override fun handle(message: String): EventInfo<GuildChat> {
            var rank = HypixelRank.DEFAULT
            enumValues<HypixelRank>().forEach {
                if (it.get().rankName in message) rank = it
            }
            val name = message.substring((message.indexOf(" > ") + 3)..message.indexOf(":")).replace(rank.get().rankName, "")
            val msg = message.substringAfter(": ")
            return EventInfo(GuildChat.javaClass, mapOf(
                    "name" to name,
                    "message" to msg,
                    "rank" to rank.name
            ))
        }

        override val pattern: Regex
            get() = "Guild\\s+>\\s+.*:\\s.*".toRegex()
    }

    /**
     * Use to intercept party chat messages
     */
    object PartyChat : ChatEventBase<PartyChat>() {
        override fun handle(message: String): EventInfo<PartyChat> {
            var rank = HypixelRank.DEFAULT
            enumValues<HypixelRank>().forEach {
                if (it.get().rankName in message) rank = it
            }
            val name = message.substring((message.indexOf(" > ") + 3)..message.indexOf(":")).replace(rank.get().rankName, "")
            val msg = message.substringAfter(": ")
            return EventInfo(PartyChat.javaClass, mapOf(
                    "name" to name,
                    "message" to msg,
                    "rank" to rank.name
            ))
        }

        override val pattern: Regex
            get() = "Party\\s+>\\s+.*:\\s.*".toRegex()
    }

    class EventInfo<T>(val type: Class<T>, val data: Map<String, String>)

}
