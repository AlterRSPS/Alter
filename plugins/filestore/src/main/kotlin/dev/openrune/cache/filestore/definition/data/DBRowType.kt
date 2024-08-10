package dev.openrune.cache.filestore.definition.data

import dev.openrune.cache.filestore.definition.Definition
import dev.openrune.cache.util.ScriptVarType

class DBRowType(override var id: Int = -1, override var inherit: Int = -1): Definition {
    var tableId = 0
    var columnTypes: Array<Array<ScriptVarType>?>? = null
    var columnValues: Array<Array<Any?>?>? = null
}