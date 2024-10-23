package dev.openrune.cache.tools.tasks

import com.displee.cache.CacheLibrary
import dev.openrune.cache.filestore.Cache

abstract class CacheTask {

    abstract fun init(library : CacheLibrary)

}