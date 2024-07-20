package org.alter

import org.alter.scrapper.datascrapper.npcs.NpcStatDumper

/**
 *     val Data_output = Path.of("./data/NewShit/")
 *     val url = "http://oldschoolrunescape.wikia.com/wiki/"
 * Actl yh --> We want to also have formaters <-- uhh unsure what could be the right word for it
 * but we want to parse the data and serialize it as (?) String -> So that we could move around the data and shit.
 * Just in case we would decide to swap out data formats.
 *
 * Or also create a migrator (Data convertor) but that shit would be barelly usable
 */
fun main() {
    NpcStatDumper.getStats()
}