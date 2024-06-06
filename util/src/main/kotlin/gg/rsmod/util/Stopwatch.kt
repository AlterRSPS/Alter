/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package gg.rsmod.util

import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * An object that accurately measures *elapsed time*: the measured duration between two
 * successive readings of "now" in the same process.
 *
 *
 * In contrast, *wall time* is a reading of "now" as given by a method like
 * [System.currentTimeMillis], best represented as an [java.time.Instant]. Such values
 * *can* be subtracted to obtain a `Duration` (such as by `Duration.between`), but
 * doing so does *not* give a reliable measurement of elapsed time, because wall time readings
 * are inherently approximate, routinely affected by periodic clock corrections. Because this class
 * uses [System.nanoTime], it is unaffected by these changes.
 *
 *
 * Use this class instead of direct calls to [System.nanoTime] for the following reason:
 *
 *
 *  * The raw `long` values returned by `nanoTime` are meaningless and unsafe to use
 * in any other way than how `Stopwatch` uses them.
 *
 *
 *
 * The one downside of `Stopwatch` relative to [System.nanoTime] is that `Stopwatch` requires object allocation and additional method calls, which can reduce the accuracy
 * of the elapsed times reported. `Stopwatch` is still suitable for logging and metrics where
 * reasonably accurate values are sufficient. If the uncommon case that you need to maximize
 * accuracy, use `System.nanoTime()` directly instead.
 *
 *
 * Basic usage:
 *
 * <pre>`Stopwatch stopwatch = Stopwatch.createStarted();
 * doSomething();
 * stopwatch.stop(); // optional
 *
 * Duration duration = stopwatch.elapsed();
 *
 * log.info("time: " + stopwatch); // formatted string like "12.3 ms"
`</pre> *
 *
 *
 * The state-changing methods are not idempotent; it is an error to start or stop a stopwatch
 * that is already in the desired state.
 *
 *
 * **Note:** This class is not thread-safe.
 *
 *
 * @author Kevin Bourrillion
 * @since 10.0
 *
 * Changes: 5/27/2024 converted com.google.common.base.Stopwatch to kotlin
 * Original file from commit c4b883de9679dae7da831e49dd9adaca71cc1991
 */
class Stopwatch {
    /**
     * Returns `true` if [.start] has been called on this stopwatch, and [.stop]
     * has not been called since the last call to `start()`.
     */
    private var isRunning = false
    private var elapsedNanos: Long = 0
    private var startTick: Long = 0

    /**
     * Starts the stopwatch.
     *
     * @return this `Stopwatch` instance
     * @throws IllegalStateException if the stopwatch is already running.
     */
    fun start(): Stopwatch {
        check(!isRunning) { "This stopwatch is already running." }
        isRunning = true
        startTick = System.nanoTime()
        return this
    }

    /**
     * Stops the stopwatch. Future reads will return the fixed duration that had elapsed up to this
     * point.
     *
     * @return this `Stopwatch` instance
     * @throws IllegalStateException if the stopwatch is already stopped.
     */
    fun stop(): Stopwatch {
        val tick = System.nanoTime()
        check(isRunning) { "This stopwatch is already stopped." }
        isRunning = false
        elapsedNanos += tick - startTick
        return this
    }

    /**
     * Sets the elapsed time for this stopwatch to zero, and places it in a stopped state.
     *
     * @return this `Stopwatch` instance
     */
    fun reset(): Stopwatch {
        elapsedNanos = 0
        isRunning = false
        return this
    }

    private fun elapsedNanos(): Long {
        return if (isRunning) System.nanoTime() - startTick + elapsedNanos else elapsedNanos
    }

    /**
     * Returns the current elapsed time shown on this stopwatch, expressed in the desired time unit,
     * with any fraction rounded down.
     *
     *
     * **Note:** the overhead of measurement can be more than a microsecond, so it is generally
     * not useful to specify [TimeUnit.NANOSECONDS] precision here.
     *
     *
     * It is generally not a good idea to use an ambiguous, unitless `long` to represent
     * elapsed time. Therefore, we recommend using [.elapsed] instead, which returns a
     * strongly-typed `Duration` instance.
     *
     * @since 14.0 (since 10.0 as `elapsedTime()`)
     */
    fun elapsed(desiredUnit: TimeUnit): Long {
        return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS)
    }

    /**
     * Returns the current elapsed time shown on this stopwatch as a [Duration]. Unlike [ ][.elapsed], this method does not lose any precision due to rounding.
     *
     * @since 22.0
     */
    fun elapsed(): Duration {
        return Duration.ofNanos(elapsedNanos())
    }

    /** Returns a string representation of the current elapsed time.  */
    override fun toString(): String {
        val nanos = elapsedNanos()
        val unit = chooseUnit(nanos)
        val value = nanos.toDouble() / TimeUnit.NANOSECONDS.convert(1, unit)

        // Too bad this functionality is not exposed as a regular method call
        return formatCompact4Digits(value) + " " + abbreviate(unit)
    }

    companion object {
        /**
         * Creates (but does not start) a new stopwatch using [System.nanoTime] as its time source.
         *
         * @since 15.0
         */
        fun createUnstarted(): Stopwatch {
            return Stopwatch()
        }

        /**
         * Creates (and starts) a new stopwatch using [System.nanoTime] as its time source.
         *
         * @since 15.0
         */
        fun createStarted(): Stopwatch {
            return Stopwatch().start()
        }

        private fun chooseUnit(nanos: Long): TimeUnit {
            if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.DAYS
            }
            if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.HOURS
            }
            if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.MINUTES
            }
            if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.SECONDS
            }
            if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.MILLISECONDS
            }
            return if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                TimeUnit.MICROSECONDS
            } else {
                TimeUnit.NANOSECONDS
            }
        }

        private fun abbreviate(unit: TimeUnit): String {
            return when (unit) {
                TimeUnit.NANOSECONDS -> "ns"
                TimeUnit.MICROSECONDS -> "\u03bcs" // μs
                TimeUnit.MILLISECONDS -> "ms"
                TimeUnit.SECONDS -> "s"
                TimeUnit.MINUTES -> "min"
                TimeUnit.HOURS -> "h"
                TimeUnit.DAYS -> "d"
                else -> throw AssertionError()
            }
        }

        fun formatCompact4Digits(value: Double): String {
            return String.format(Locale.ROOT, "%.4g", value)
        }
    }
}
