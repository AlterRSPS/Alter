package org.alter.game.model.skill

import org.alter.game.model.varp.Varp
import org.bson.Document

/**
 * Represents a trainable skill for a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Skill(val id: Int, var xp: Double = 0.0, var currentLevel: Int = 1) {

    fun asDocument(): Document {
        val doc = Document()
        doc.append("id",id)
        doc.append("level",currentLevel)
        doc.append("xp",xp)
        return doc
    }

    companion object {
        fun fromDocument(doc: Document) : Skill {
            return Skill(doc.getInteger("id"),doc.getDouble("xp"),doc.getInteger("level"))
        }
    }

}
