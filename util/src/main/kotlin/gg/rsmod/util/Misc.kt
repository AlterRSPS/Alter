package gg.rsmod.util

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Misc {
    fun IntRange.toArray(): Array<Int> {
        return toList().toTypedArray()
    }

    fun getIndefiniteArticle(word: String): String {
        val first = word.lowercase().first()
        val vowel = first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u'
        val numeric = word.first().equals(Regex(".*[0-9].*"))
        val special =
            listOf(
                "bolts",
                "arrows",
                "coins",
                "vambraces",
                "chaps",
                "grapes",
                "silk",
                "bread",
                "grey wolf fur",
                "spice",
            ).filter { it in word }
        val some = special.isNotEmpty()
        return (
            if (numeric) {
                ""
            } else if (vowel) {
                "an"
            } else if (some) {
                "some"
            } else {
                "a"
            }
        ) + " " + word
    }
}
