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
package gg.rsmod.util

/**
 * Extension function that creates an instance of [ToStringHelper].
 *
 * This is helpful for implementing [Object.toString]. Specification by example:
 *
 * toStringHelper().toString();  // Returns "ClassName{}"
 *
 * toStringHelper().add("x", 1).toString();  // Returns "ClassName{x=1}"
 *
 *
 * toStringHelper().add("x", 1).add("y", "foo").toString(); // Returns "ClassName{x=1, y=foo}"
 *
 */
fun Any.toStringHelper(): ToStringHelper {
    return ToStringHelper(javaClass.simpleName)
}

/**
 * Support class for [toStringHelper].
 *
 * Use [toStringHelper()] to create an instance. Do not instantiate this directly.
 *
 * @author Jason Lee
 * @since 18.0 (since 2.0 as `Objects.ToStringHelper`).
 *
 * Changes: 5/27/2024 converted com.google.common.base.MoreObjects.ToStringHelper to kotlin
 * Streamlined functionality and removed excluding nulls.
 * Original file from commit c4b883de9679dae7da831e49dd9adaca71cc1991
 */
class ToStringHelper constructor(private val className: String) {
    private val holderHead = ValueHolder()
    private var holderTail: ValueHolder? = holderHead

    /**
     * Adds a name/value pair to the formatted output in `name=value` format. If `value`
     * is `null`, the string `"null"` is used
     */
    fun add(
        name: String,
        value: Any?,
    ): ToStringHelper {
        val valueHolder = ValueHolder()
        holderTail!!.next = valueHolder
        holderTail = holderTail!!.next
        valueHolder.value = value
        valueHolder.name = name
        return this
    }

    /**
     * Returns a string in the format specified by [toStringHelper.toStringHelper].
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
                val arrayString = arrayOf(value).contentDeepToString()
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
