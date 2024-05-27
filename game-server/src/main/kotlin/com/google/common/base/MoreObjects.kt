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

//import static com.google.common.base.Preconditions.checkNotNull;
//
//import com.google.common.annotations.GwtCompatible;
//import com.google.errorprone.annotations.CanIgnoreReturnValue;
/**
 * Helper functions that operate on any `Object`, and are not already provided in
 * [java.util.Objects].
 *
 *
 * See the Guava User Guide on
 * [writing
 * `Object` methods with `MoreObjects`](https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained).
 *
 * @author Laurence Gonsalves
 * @since 18.0 (since 2.0 as `Objects`)
 */
//@GwtCompatible
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
     * Creates an instance of [ToStringHelper] in the same manner as
     * [.toStringHelper], but using the simple name of `clazz` instead of using an
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
     * Creates an instance of [ToStringHelper] in the same manner as
     * [.toStringHelper], but using `className` instead of using an instance's
     * [Object.getClass].
     *
     * @param className the name of the instance type
     * @since 18.0 (since 7.0 as `Objects.toStringHelper()`).
     */
    fun toStringHelper(className: String): ToStringHelper {
        return ToStringHelper(className)
    }

    //TODO ADDED
    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }

    /**
     * Support class for [MoreObjects.toStringHelper].
     *
     * @author Jason Lee
     * @since 18.0 (since 2.0 as `Objects.ToStringHelper`).
     */
    class ToStringHelper(className: String) {
        private val className: String
        private val holderHead = ValueHolder()
        private var holderTail: ValueHolder? = holderHead
        private var omitNullValues = false

        /**
         * Use [MoreObjects.toStringHelper] to create an instance.
         */
        init {
            this.className = checkNotNull(className)
        }

        /**
         * Configures the [ToStringHelper] so [.toString] will ignore properties with null
         * value. The order of calling this method, relative to the `add()`/`addValue()`
         * methods, is not significant.
         *
         * @since 18.0 (since 12.0 as `Objects.ToStringHelper.omitNullValues()`).
         */
        //    @CanIgnoreReturnValue
        fun omitNullValues(): ToStringHelper {
            omitNullValues = true
            return this
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format. If `value`
         * is `null`, the string `"null"` is used, unless [.omitNullValues] is
         * called, in which case this name/value pair will not be added.
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Any?): ToStringHelper {
            return addHolder(name, value)
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Boolean): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Char): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Double): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Float): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Int): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds a name/value pair to the formatted output in `name=value` format.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.add()`).
         */
        //    @CanIgnoreReturnValue
        fun add(name: String, value: Long): ToStringHelper {
            return addHolder(name, value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Any?): ToStringHelper {
            return addHolder(value)
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Boolean): ToStringHelper {
            return addHolder(value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Char): ToStringHelper {
            return addHolder(value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Double): ToStringHelper {
            return addHolder(value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Float): ToStringHelper {
            return addHolder(value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Int): ToStringHelper {
            return addHolder(value.toString())
        }

        /**
         * Adds an unnamed value to the formatted output.
         *
         *
         * It is strongly encouraged to use [.add] instead and give value a
         * readable name.
         *
         * @since 18.0 (since 11.0 as `Objects.ToStringHelper.addValue()`).
         */
        //    @CanIgnoreReturnValue
        fun addValue(value: Long): ToStringHelper {
            return addHolder(value.toString())
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
            val omitNullValuesSnapshot = omitNullValues
            var nextSeparator = ""
            val builder = StringBuilder(32).append(className).append('{')
            var valueHolder = holderHead.next
            while (valueHolder != null) {
                val value = valueHolder.value
                if (!omitNullValuesSnapshot || value != null) {
                    builder.append(nextSeparator)
                    nextSeparator = ", "
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=')
                    }
                    if (value != null && value.javaClass.isArray) {
                        val objectArray = arrayOf(value)
                        val arrayString = objectArray.contentDeepToString()
                        builder.append(arrayString, 1, arrayString.length - 1)
                    } else {
                        builder.append(value)
                    }
                }
                valueHolder = valueHolder.next
            }
            return builder.append('}').toString()
        }

        private fun addHolder(): ValueHolder {
            val valueHolder = ValueHolder()
            holderTail!!.next = valueHolder
            holderTail = holderTail!!.next
            return valueHolder
        }

        private fun addHolder(value: Any?): ToStringHelper {
            val valueHolder = addHolder()
            valueHolder.value = value
            return this
        }

        private fun addHolder(name: String, value: Any?): ToStringHelper {
            val valueHolder = addHolder()
            valueHolder.value = value
            valueHolder.name = checkNotNull(name)
            return this
        }

        private class ValueHolder {
            var name: String? = null
            var value: Any? = null
            var next: ValueHolder? = null
        }
    }
}
