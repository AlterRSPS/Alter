package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.util.ScriptVarType

class DBTableType(override var id: Int = -1, override var inherit: Int = -1): Definition {
    var types: Array<Array<ScriptVarType>?>? = null
    var defaultColumnValues: Array<Array<Any?>?>? = null
}