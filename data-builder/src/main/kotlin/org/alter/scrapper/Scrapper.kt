package org.alter.scrapper

import java.nio.file.Path

interface Scrapper {
    fun init(output: Path) {}

    fun postLoad() {}
}