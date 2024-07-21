package org.alter.scrapper.datascrapper.npcs

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Hmm, think we could convert this stuff into separate classes.\
 * Need to look into how others r doing stuff like that
 */
object NpcStatDumper {
    fun getStats() {
        val basic_url = "https://oldschool.runescape.wiki/w/Special:Lookup?type=npc&id="
        var mob_id = 239
        val url = basic_url + mob_id
        val document: Document = Jsoup.connect(url).get()
        val tables: Elements = document.getElementsByClass("infobox-monster")
            tables.forEach {
                val name = it.getElementsByClass("infobox-header").text()
                println("=============$name:$mob_id=============")
                val rows = it.select("td[colspan=16]")
                println("=============td[colspan=16]=============")
                rows.forEachIndexed { index, it ->
                    println("$index [${it.text()}]")
                }
                println("=============[infobox-nested]=============")
                val classes = it.getElementsByClass("infobox-nested")
                classes.forEachIndexed { index, it ->
                    println("$index [${it.text()}]")
                }
            }
    }







    fun getDrops() {
        /**
         * Test url: https://oldschool.runescape.wiki/w/King_Black_Dragon
         */
        val url = "https://oldschool.runescape.wiki/w/Lesser_demon#Level_94"
        val document: Document = Jsoup.connect(url).get()
        val tables: Elements = document.select("table.wikitable.sortable.filterable.item-drops")
        for (table in tables) {
            val rows: Elements = table.select("tbody > tr")
            for (row in rows) {
                val columns: Elements = row.select("td")
                if (columns.size == 6) {
                    var rarityElement = columns[3].select("span[data-drop-oneover]")
                    if (rarityElement?.text() == null) {
                        rarityElement = columns[3].select("span")
                    }
                    var dropFraction = rarityElement?.text()
                    val itemName = columns[1].text()
                    val quantity = columns[2].text()
                    // Print the extracted data
                    println("Item Name: $itemName")
                    println("Quantity: $quantity")
                    println(dropFraction)
                    println("-----------")
                }
            }
        }
    }
}