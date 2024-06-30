/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Joshua Filby <joshua@filby.me>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gg.rsmod.util

import java.util.Locale

class Namer {
    private val used: MutableSet<String> = HashSet()

    fun name(
        name: String?,
        id: Int,
    ): String? {
        var name = name
        name = sanitize(name)
        if (name == null) {
            return null
        }
        if (used.contains(name)) {
            name = name + "_" + id
            assert(!used.contains(name))
        }
        used.add(name)
        return name
    }

    companion object {
        private fun sanitize(`in`: String?): String? {
            val s =
                removeTags(`in`)
                    .uppercase(Locale.getDefault())
                    .replace(' ', '_')
                    .replace("[^a-zA-Z0-9_]".toRegex(), "")
            if (s.isEmpty()) {
                return null
            }
            return if (Character.isDigit(s[0])) {
                "_$s"
            } else {
                s
            }
        }

        fun removeTags(str: String?): String {
            val builder = StringBuilder(str!!.length)
            var inTag = false
            for (i in 0 until str.length) {
                val currentChar = str[i]
                if (currentChar == '<') {
                    inTag = true
                } else if (currentChar == '>') {
                    inTag = false
                } else if (!inTag) {
                    builder.append(currentChar)
                }
            }
            return builder.toString()
        }
    }
}
