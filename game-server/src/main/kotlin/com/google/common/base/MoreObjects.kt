/*
 * Copyright (C) 2014 The Guava Authors
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

import java.util.*

/**
 * Helper functions that operate on any `Object`, and are not already provided in [ ].
 *
 *
 * See the Guava User Guide on [writing `Object`
 * methods with `MoreObjects`](https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained).
 *
 * @author Laurence Gonsalves
 * @since 18.0 (since 2.0 as `Objects`)
 */
object MoreObjects {

    /**
     * Creates an instance of [ToStringHelper].
     *
     *
     * This is helpful for implementing [Object.toString]. Specification by example:
     *
     * <pre>`// Returns "ClassName{}"
     * MoreObjects.toStringHelper(this)
     * .toString();
     *
     * // Returns "ClassName{x=1}"
     * MoreObjects.toStringHelper(this)
     * .add("x", 1)
     * .toString();
     *
     * // Returns "MyObject{x=1}"
     * MoreObjects.toStringHelper("MyObject")
     * .add("x", 1)
     * .toString();
     *
     * // Returns "ClassName{x=1, y=foo}"
     * MoreObjects.toStringHelper(this)
     * .add("x", 1)
     * .add("y", "foo")
     * .toString();
     *
     * // Returns "ClassName{x=1}"
     * MoreObjects.toStringHelper(this)
     * .omitNullValues()
     * .add("x", 1)
     * .add("y", null)
     * .toString();
    `</pre> *
     *
     *
     * Note that in GWT, class names are often obfuscated.
     *
     * @param self the object to generate the string for (typically `this`), used only for its
     * class name
     * @since 18.0 (since 2.0 as `Objects.toStringHelper()`).
     */
    fun toStringHelper(self: Any): ToStringHelper {
        return ToStringHelper(self.javaClass.simpleName)
    }

    /**
     * Creates an instance of [ToStringHelper] in the same manner as [ ][.toStringHelper], but using the simple name of `clazz` instead of using an
     * instance's [Object.getClass].
     *
     *
     * Note that in GWT, class names are often obfuscated.
     *
     * @param clazz the [Class] of the instance
     * @since 18.0 (since 7.0 as `Objects.toStringHelper()`).
     */
    fun toStringHelper(clazz: Class<*>): ToStringHelper {
        return ToStringHelper(clazz.simpleName)
    }

    /**
     * Creates an instance of [ToStringHelper] in the same manner as [ ][.toStringHelper], but using `className` instead of using an instance's [ ][Object.getClass].
     *
     * @param className the name of the instance type
     * @since 18.0 (since 7.0 as `Objects.toStringHelper()`).
     */
    fun toStringHelper(className: String): ToStringHelper {
        return ToStringHelper(className)
    }

    /**
     * Support class for [MoreObjects.toStringHelper].
     *
     * @author Jason Lee
     * @since 18.0 (since 2.0 as `Objects.ToStringHelper`).
     */
    /** Use [MoreObjects.toStringHelper] to create an instance.  */
    class ToStringHelper constructor(private val className: String) {
        private val holderHead = ValueHolder()
        private var holderTail: ValueHolder? = holderHead

        /**
         * Adds a name/value pair to the formatted output in `name=value` format. If `value`
         * is `null`, the string `"null"` is used, unless [.omitNullValues] is
         * called, in which case this name/value pair will not be added.
         */
        fun add(name: String, value: Any?): ToStringHelper {
            val valueHolder = ValueHolder()
            holderTail!!.next = valueHolder
            holderTail = holderTail!!.next
            valueHolder.value = value
            valueHolder.name = name
            return this
        }

        /**
         * Returns a string in the format specified by [MoreObjects.toStringHelper].
         *
         *
         * After calling this method, you can keep adding more properties to later call toString()
         * again and get a more complete representation of the same object; but properties cannot be
         * removed, so this only allows limited reuse of the helper instance. The helper allows
         * duplication of properties (multiple name/value pairs with the same name can be added).
         */
        override fun toString(): String {
            // create a copy to keep it consistent in case value changes
            var nextSeparator = ""
            val builder = StringBuilder(32).append(className).append('{')
            var valueHolder = holderHead.next
            while (valueHolder != null) {
                val value = valueHolder.value
                builder.append(nextSeparator)
                nextSeparator = ", "
                if (valueHolder.name != null) {
                    builder.append(valueHolder.name).append('=')
                }
                if (value != null && value.javaClass.isArray) {
                    val objectArray = arrayOf(value)
                    val arrayString = Arrays.deepToString(objectArray)
                    builder.append(arrayString, 1, arrayString.length - 1)
                } else {
                    builder.append(value)
                }
                valueHolder = valueHolder.next
            }
            return builder.append('}').toString()
        }

        // Holder object for values that might be null and/or empty.
        private class ValueHolder {
            var name: String? = null
            var value: Any? = null
            var next: ValueHolder? = null
        }
    }
}