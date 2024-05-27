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
        val special = listOf("bolts", "arrows", "coins", "vambraces", "chaps", "grapes", "silk", "bread", "grey wolf fur", "spice").filter { it in word }
        val some = special.isNotEmpty()
        return (if (numeric) "" else if (vowel) "an" else if (some) "some" else "a") + " " + word
    }

    /**
     * Returns the greatest value present in `array`.
     *
     * @param array a *nonempty* array of `int` values
     * @return the value present in `array` that is greater than or equal to every other value
     * in the array
     * @throws IllegalArgumentException if `array` is empty
     */
    fun max(vararg array: Int): Int {
        require(array.isNotEmpty())
        var max = array[0]
        for (i in 1 until array.size) {
            if (array[i] > max) {
                max = array[i]
            }
        }
        return max
    }
}