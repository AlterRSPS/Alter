package org.alter.game.saving.formats

import org.alter.game.saving.formats.impl.Json
import org.alter.game.saving.formats.impl.Mongo
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

enum class SaveFormatType(val type: KClass<out FormatHandler>) {
    JSON(Json::class),
    MONGO(Mongo::class);

    // Method to create an instance of the corresponding serialization class
    inline fun <reified T : FormatHandler> createInstance(param: String): T {
        // Retrieve the primary constructor of the class
        val constructor = type.primaryConstructor
            ?: throw IllegalArgumentException("Class ${type.simpleName} must have a primary constructor")

        // Check if the primary constructor takes exactly one string parameter
        if (constructor.parameters.size != 1 || constructor.parameters[0].type.classifier != String::class) {
            throw IllegalArgumentException("Primary constructor of ${type.simpleName} must take exactly one String parameter")
        }

        // Create an instance with the provided parameter
        return constructor.call(param) as T
    }
}
