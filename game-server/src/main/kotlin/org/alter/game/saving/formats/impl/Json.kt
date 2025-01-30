package org.alter.game.saving.formats.impl

import org.alter.game.model.entity.Client
import org.alter.game.saving.formats.FormatHandler
import org.bson.Document
import org.bson.json.JsonWriterSettings
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*

class Json(override val collectionName: String) : FormatHandler(collectionName) {

    private var path: Path = Path("")

    private var prettyPrintSettings: JsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

    init {
        path = Paths.get("../data/saves/${collectionName}/")
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }
    }

    override fun saveDocument(client: Client, document: Document) {
        path.resolve(client.loginUsername).writeText(document.toJson(prettyPrintSettings))
    }

    override fun parseDocument(client : Client): Document {
        return Document.parse(path.resolve(client.loginUsername).readText())
    }

    override fun loadAll(): Map<String, Document> {
        val doc = mutableMapOf<String, Document>()
        Files.list(path).use { stream ->
            stream.filter { Files.isRegularFile(it) }.forEach { path ->
                doc[path.fileName.toString().substringBeforeLast(".")] = Document.parse(Files.readString(path))
            }
        }
        return doc
    }

    override fun playerExists(client: Client): Boolean {
        val save = path.resolve(client.loginUsername)
        return Files.exists(save)
    }

}