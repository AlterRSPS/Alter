package org.alter.game.playersaving.formats

import org.alter.game.model.entity.Client
import org.bson.Document

abstract class FormatHandler {

    open fun init() {}

    abstract fun saveDocument(client : Client, document : Document)

    abstract fun playerExists(client : Client) : Boolean

    abstract fun parseDocument(client : Client) : Document



}