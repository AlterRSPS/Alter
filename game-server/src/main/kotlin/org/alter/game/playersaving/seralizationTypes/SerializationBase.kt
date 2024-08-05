package org.alter.game.playersaving.seralizationTypes

import org.alter.game.model.entity.Client
import org.bson.Document

abstract class SerializationBase {

    abstract fun saveDocument(client : Client, document : Document)

    abstract fun playerExist(username : String) : Boolean

    abstract fun parseDocument(client : Client) : Document



}