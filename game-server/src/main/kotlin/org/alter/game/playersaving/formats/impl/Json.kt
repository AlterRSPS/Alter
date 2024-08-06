package org.alter.game.playersaving.formats.impl

import org.alter.game.model.entity.Client
import org.alter.game.playersaving.formats.FormatHandler
import org.bson.Document
import org.bson.json.JsonWriterSettings
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

class Json : FormatHandler() {

    private var path: Path = Path("../data/saves/")

    private var prettyPrintSettings: JsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

    init {
        if (!path.exists()) {
            path.createDirectory()
        }
    }

    override fun saveDocument(client: Client, document: Document) {
        path.resolve(client.loginUsername).writeText(document.toJson(prettyPrintSettings))
    }

    override fun parseDocument(client : Client): Document {
        return Document.parse(path.resolve(client.loginUsername).readText())
    }

    override fun playerExists(client: Client): Boolean {
        val save = path.resolve(client.loginUsername)
        return Files.exists(save)
    }

}