package org.alter.game.saving.impl

import org.alter.game.model.Tile
import org.alter.game.model.entity.Client
import org.alter.game.model.interf.DisplayMode
import org.alter.game.model.priv.Privilege
import org.alter.game.saving.DocumentHandler
import org.bson.Document

class DetailSerialisation(override val name: String = "details") : DocumentHandler {

    override fun fromDocument(client: Client, doc: Document) {
        client.tile = Tile(doc["tile"] as List<Int>)
        client.privilege = client.world.privileges.get(doc.getString("privilege"))?: Privilege.DEFAULT
        client.runEnergy = doc.getDouble("runEnergy") ?: 10000.00
        client.interfaces.displayMode = DisplayMode.values.firstOrNull { it.name == doc.getString("displayMode") } ?: DisplayMode.FIXED
    }

    override fun asDocument(client: Client): Document = Document()
        .append("tile", listOf(client.tile.x, client.tile.z, client.tile.height))
        .append("privilege", client.privilege.name.uppercase())
        .append("runEnergy", client.runEnergy)
        .append("displayMode", client.interfaces.displayMode.name.uppercase())

}