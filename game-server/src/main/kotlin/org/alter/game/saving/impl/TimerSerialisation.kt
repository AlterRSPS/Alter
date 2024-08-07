package org.alter.game.saving.impl

import org.alter.game.model.entity.Client
import org.alter.game.model.timer.TimerKey
import org.alter.game.model.timer.TimerMap
import org.alter.game.saving.DocumentHandler
import org.bson.Document

class TimerSerialisation(override val name: String = "timers") : DocumentHandler {

    override fun fromDocument(client: Client, doc: Document) {
        doc.forEach { (identifier, value) ->
            val timer = TimerMap.PersistentTimer.fromDocument(identifier,value as Document)
            var time = timer.timeLeft
            if (timer.tickOffline) {
                val elapsed = System.currentTimeMillis() - timer.currentMs
                val ticks = (elapsed / client.world.gameContext.cycleTime).toInt()
                time -= ticks
            }
            val key = TimerKey(timer.identifier, timer.tickOffline)
            client.timers[key] = 0.coerceAtLeast(time)
        }
    }

    override fun asDocument(client: Client): Document {
        return Document().apply {
            client.timers.toPersistentTimers().forEach { timer ->
                append(timer.identifier, timer.asDocument())
            }
        }
    }

}