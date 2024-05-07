package org.alter.game.message

import org.alter.game.model.World
import org.alter.game.model.entity.Client

object MessageTypeRegistry {
    private val handlers: MutableMap<Class<*>, MessageHandler<*>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getHandler(messageType: Class<T>): MessageHandler<T>? {
        return handlers[messageType] as? MessageHandler<T>
    }

    fun <T : Any> registerHandler(messageType: Class<T>, handler: MessageHandler<T>) {
        handlers[messageType] = handler
    }

    fun getAllHandlers(): Map<Class<*>, MessageHandler<*>> = handlers
}

fun dispatchMessage(message: Any, client: Client, world: World) {
    val handler = MessageTypeRegistry.getHandler(message::class.java)
    if (handler != null) {
        (handler as MessageHandler<Any>).accept(client, message)
    } else {
        println("No handler found for message type: ${message::class.java}")
    }
}