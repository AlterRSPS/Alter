package dev.openrune.cache.util

import java.util.*

class Namer {
    private val used: MutableSet<String> = HashSet()

    fun name(name: String?, id: Int): String? {
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
            val s = removeTags(`in`)
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