package org.alter.game.playersaving.seralizationTypes.impl

import org.alter.game.model.entity.Client
import org.alter.game.playersaving.seralizationTypes.SerializationBase
import org.bson.Document
import org.bson.json.JsonWriterSettings
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

class JsonSerialization : SerializationBase() {

    private var prettyPrintSettings: JsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

    private var path: Path = Path("../data/saves/")


    override fun saveDocument(player: Client, document: Document) {
        path.resolve(player.loginUsername).writeText(document.toJson(prettyPrintSettings))
    }

    override fun parseDocument(client : Client): Document {
        return Document.parse(path.resolve(client.loginUsername).readText())
    }

    override fun playerExist(username: String): Boolean {
        val save = path.resolve(username)
        return Files.exists(save)
    }

}