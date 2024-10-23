package org.alter.api.ext

import org.alter.game.model.Area
import org.alter.game.model.Tile
import org.alter.game.model.timer.TimeConstants
import org.alter.game.model.timer.TimerKey
import org.alter.game.model.timer.TimerMap
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.Format
import java.util.*
import java.util.concurrent.ThreadLocalRandom

private val RANDOM = ThreadLocalRandom.current()
val random: Random = SecureRandom()
fun Number.appendToString(string: String): String = "$this $string" + (if (this != 1) "s" else "")

fun Number.format(format: Format): String = format.format(this)

fun Number.decimalFormat(): String = format(DecimalFormat())

fun String.parseAmount(): Long =
    when {
        endsWith("k") -> substring(0, length - 1).toLong() * 1000
        endsWith("m") -> substring(0, length - 1).toLong() * 1_000_000
        endsWith("b") -> substring(0, length - 1).toLong() * 1_000_000_000
        else -> substring(0, length).toLong()
    }


fun random(boundInclusive: Int) = random.nextInt(boundInclusive)

fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start) + range.start
fun randomStep(start: Int, stop: Int, step: Int): Int {
    val result = (start..stop step step).toList()
    if (result.isEmpty()) {
        return start
    }
    return (start..stop step step).toList().random()
}


fun Int.interpolate(
    minChance: Int,
    maxChance: Int,
    minLvl: Int,
    maxLvl: Int,
): Int = minChance + (maxChance - minChance) * (this - minLvl) / (maxLvl - minLvl)

fun Int.interpolate(
    minChance: Int,
    maxChance: Int,
    minLvl: Int,
    maxLvl: Int,
    cap: Int,
): Boolean = RANDOM.nextInt(cap) <= interpolate(minChance, maxChance, minLvl, maxLvl)

/**
 * Get time left from a [TimerKey], in minutes.
 *
 * @return
 * Null if the minutes left is less than 1 (one). Minutes left in timer key otherwise.
 */
fun TimerMap.getMinutesLeft(key: TimerKey): Int? = TimeConstants.cyclesToMinutes(get(key))

/**
 * Create an empty [EnumSet] of type [T].
 */
inline fun <reified T : Enum<T>> enumSetOf() = EnumSet.noneOf(T::class.java)

/**
 * Create an [EnumSet] made up of [values].
 *
 * @param values the default values stored in our set.
 */
inline fun <reified T : Enum<T>> enumSetOf(vararg values: T) = EnumSet.noneOf(T::class.java).apply { addAll(values) }

/**
 * Get a random tile within the bounds of this area. Does <strong>not</strong>
 * take into account clipped tiles within the area.
 */
val Area.randomTile: Tile get() =
    Tile(
        bottomLeftX + RANDOM.nextInt((topRightX - bottomLeftX) + 1),
        bottomLeftY + RANDOM.nextInt((topRightY - bottomLeftY) + 1),
    )
