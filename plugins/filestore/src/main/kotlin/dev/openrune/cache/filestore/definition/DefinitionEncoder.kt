package dev.openrune.cache.filestore.definition

import dev.openrune.cache.filestore.buffer.Writer

interface DefinitionEncoder<T : Definition> {
    fun Writer.encode(definition: T, members: T) {
        encode(definition)
    }

    fun Writer.encode(definition: T) {
        encode(definition, definition)
    }

    fun Writer.encodeCustom(definition: T, members: T) {
        encode(definition, definition)
    }

}