package dev.openrune

import java.io.File

object RSCMHandler {

    var mappings: Map<String, Int> = emptyMap()

    fun load(mappingsDir: File) {

        if (!mappingsDir.exists()) {
            throw IllegalStateException("Missing Mapping Files")
        }

        // Create a mutable map to hold all mappings
        val allMappings = mutableMapOf<String, Int>()

        mappingsDir.listFiles { _, name -> name.endsWith(".rscm") }?.forEach { file ->
            val type = file.nameWithoutExtension
            val infoMap = file.readLines()
                .filter { it.isNotBlank() }
                .associate { line ->
                    val (key, value) = line.split(":").takeIf { it.size == 2 }
                        ?: throw IllegalArgumentException("Invalid line format: $line")
                    "${type}.${key}" to value.toInt()
                }
            // Add the infoMap entries to the allMappings
            allMappings.putAll(infoMap)
        }

        // Assign the populated map to the mappings variable
        mappings = allMappings
    }

    fun getMapping(key: String): Int? {
        return mappings[key]
    }
}
