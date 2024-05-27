package com.google.common.primitives

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
