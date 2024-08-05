package org.alter.game.playersaving.impl

import org.alter.game.model.entity.Client
import org.alter.game.model.skill.Skill
import org.alter.game.model.skill.SkillSet
import org.alter.game.model.varp.Varp
import org.alter.game.playersaving.DocumentDecoder
import org.alter.game.playersaving.DocumentEncoder
import org.bson.Document

class SkillSerialisation(override val name: String = "skills") : DocumentDecoder, DocumentEncoder {

    override fun fromDocument(client: Client, doc: Document) = doc.forEach { _, skillDoc ->
        client.getSkills().setSkill(Skill.fromDocument(skillDoc as Document))
    }

    override fun asDocument(client: Client): Document {
        return Document().apply {
            (0 until client.getSkills().maxSkills).forEach { skillID ->
                val skill = client.getSkills()[skillID]
                val name = SkillSet.getSkillName(skillID).lowercase()
                put(name, skill.asDocument())
            }
        }
    }

}