package org.alter.scrapper

import java.nio.file.Path

interface Scrapper {
    fun init(output: Path) {
        // Loader
    }

    fun postLoad() {
        // After data was fully loaded -> post times
    }
}