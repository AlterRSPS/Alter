package org.alter.game.saving.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.alter.game.model.container.ItemContainer
import org.alter.game.model.entity.Client
import org.alter.game.model.item.Item
import org.alter.game.saving.DocumentHandler
import org.bson.Document

private val logger = KotlinLogging.logger {}
class ContainersSerialisation(override val name: String = "containers") : DocumentHandler {

    override fun fromDocument(client: Client, doc: Document) {
        doc.forEach { (containerKey, itemsList) ->
            val key = client.world.plugins.containerKeys.firstOrNull { it.name == containerKey }

            if (key == null) {
                logger.error { "Container found in serialized data, but is not registered to our World. [key=$containerKey]" }
                return@forEach
            }

            val container = client.containers.getOrPut(key) { ItemContainer(key) }

            if (itemsList is Document) {
                itemsList.forEach { (slotString, itemDoc) ->
                    val slot = slotString.toIntOrNull()
                    if (slot != null) {
                        val item = Item.fromDocument(itemDoc as Document)
                        container[slot] = item
                    }
                }
            }
        }
    }

    override fun asDocument(client: Client): Document {
        return Document().apply {
            client.getPersistentContainers().forEach { container ->
                val itemsDoc = Document()
                container.items.forEach { (slot, item) ->
                    itemsDoc.append(slot.toString(), item.asDocument())
                }
                put(container.name, itemsDoc)
            }
        }
    }

    private fun Client.getPersistentContainers(): List<PersistentContainer> {
        val persistent = mutableListOf<PersistentContainer>()

        containers.forEach { (key, container) ->
            if (!container.isEmpty) {
                persistent.add(PersistentContainer(key.name, container.toMap()))
            }
        }

        return persistent
    }

    data class PersistentContainer(val name: String, val items: Map<Int, Item>)

}