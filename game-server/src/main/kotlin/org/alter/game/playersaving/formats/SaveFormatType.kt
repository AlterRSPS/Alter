package org.alter.game.playersaving.formats

import org.alter.game.playersaving.formats.impl.Json
import org.alter.game.playersaving.formats.impl.mongo.Mongo
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

enum class SaveFormatType(val type: KClass<out FormatHandler>) {
    JSON(Json::class),
    MONGO(Mongo::class);

    // Method to create an instance of the corresponding serialization class
    fun createInstance(): FormatHandler {
        return type.createInstance()
    }
}