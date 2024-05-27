/*
 * Copyright (C) 2011 The Guava Authors
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
package com.google.common.base
//import com.google.common.annotations.Beta;
//import com.google.common.annotations.GwtCompatible;
//import com.google.errorprone.annotations.CanIgnoreReturnValue;
/**
 * A time source; returns a time value representing the number of nanoseconds elapsed since some
 * fixed but arbitrary point in time. Note that most users should use [Stopwatch] instead of
 * interacting with this class directly.
 *
 *
 * **Warning:** this interface can only be used to measure elapsed time, not wall time.
 *
 * @author Kevin Bourrillion
 * @since 10.0 ([mostly
 * source-compatible](https://github.com/google/guava/wiki/Compatibility) since 9.0)
 */
//@Beta
//@GwtCompatible
abstract class Ticker
/**
 * Constructor for use by subclasses.
 */
protected constructor() {
    /**
     * Returns the number of nanoseconds elapsed since this ticker's fixed point of reference.
     */
    //  @CanIgnoreReturnValue // TODO(kak): Consider removing this
    abstract fun read(): Long

    companion object {
        /**
         * A ticker that reads the current time using [System.nanoTime].
         *
         * @since 10.0
         */
        fun systemTicker(): Ticker {
            return SYSTEM_TICKER
        }

        private val SYSTEM_TICKER: Ticker = object : Ticker() {
            override fun read(): Long {
                return 
            }
        }
    }
}
